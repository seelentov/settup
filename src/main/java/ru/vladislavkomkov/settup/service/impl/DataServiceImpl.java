package ru.vladislavkomkov.settup.service.impl;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vladislavkomkov.settup.exception.DataParseException;
import ru.vladislavkomkov.settup.exception.NotFoundException;
import ru.vladislavkomkov.settup.model.data.*;
import ru.vladislavkomkov.settup.model.query.*;
import ru.vladislavkomkov.settup.repository.DataEntityRepository;
import ru.vladislavkomkov.settup.repository.DataFieldRepository;
import ru.vladislavkomkov.settup.repository.DataTopicRepository;
import ru.vladislavkomkov.settup.repository.specification.DataEntitySpecifications;
import ru.vladislavkomkov.settup.service.DataService;

@Service
public class DataServiceImpl implements DataService {
    private final DataTopicRepository topicRepository;
    private final DataEntityRepository entityRepository;
    private final DataFieldRepository fieldRepository;

    public DataServiceImpl(DataTopicRepository topicRepository, DataEntityRepository entityRepository, DataFieldRepository fieldRepository) {
        this.topicRepository = topicRepository;
        this.entityRepository = entityRepository;
        this.fieldRepository = fieldRepository;
    }

    @Override
    public List<DataTopic> getTopics() {
        return topicRepository.findAll();
    }

    @Override
    public List<DataEntity> getTopic(String topicName) {
        return entityRepository.findByTopicName(topicName);
    }

    @Override
    public Optional<DataEntity> getEntity(int id) {
        return entityRepository.findById(id);
    }

    @Override
    public DataTopic addTopic(String topicName, List<DataTopicField> scheme) {
        DataTopic topic = new DataTopic();
        topic.setName(topicName);

        for (DataTopicField field : scheme) {
            field.setTopic(topic);
        }

        topic.setScheme(scheme);
        return topicRepository.save(topic);
    }

    @Override
    public void addEntity(String topicName, List<DataField> dataFields) {
        Optional<DataTopic> topic = topicRepository.findByName(topicName);
        if (topic.isEmpty()) {
            throw new NotFoundException("Topic " + topicName + " not found");
        }

        DataEntity entity = new DataEntity();
        entity.setTopic(topic.get());

        for (DataField field : dataFields) {
            field.setEntity(entity);
        }

        entity.setFields(dataFields);

        entityRepository.save(entity);
    }

    @Override
    @Transactional
    public void editEntity(int id, Map<String, String> data) {
        Optional<DataEntity> entityOpt = getEntity(id);
        if (entityOpt.isEmpty()) {
            throw new NotFoundException("Entity by id " + id + " not found");
        }

        List<DataField> existingFields = fieldRepository.findByEntityIdAndNameIn(id, data.keySet());

        Map<String, DataField> fieldMap = existingFields.stream().collect(Collectors.toMap(DataField::getName, Function.identity()));

        for (String fieldName : data.keySet()) {
            if (!fieldMap.containsKey(fieldName)) {
                throw new NotFoundException("Field '" + fieldName + "' in entity by id " + id + " not found");
            }
        }

        for (var entry : data.entrySet()) {
            DataField field = fieldMap.get(entry.getKey());
            String newValue = entry.getValue();
            if (!Objects.equals(field.getDataValue(), newValue)) {
                field.setDataValue(newValue);
            }
        }
    }

    @Transactional(readOnly = true)
    public Page<DataEntity> executeTypedQuery(QueryRequest queryRequest) {
        validateQueryRequest(queryRequest);

        Specification<DataEntity> spec = createTypedSpecification(queryRequest);

        Pageable pageable = createTypedPageable(queryRequest);

        return entityRepository.findAll(spec, pageable);
    }

    private void validateQueryRequest(QueryRequest queryRequest) {
        if (queryRequest.getTopicId() == null) {
            throw new IllegalArgumentException("Topic ID is required");
        }

        Optional<DataTopic> topicOpt = topicRepository.findById(queryRequest.getTopicId());
        if (topicOpt.isEmpty()) {
            throw new IllegalArgumentException("Topic not found");
        }

        DataTopic topic = topicOpt.get();
        Map<String, DataFieldType> fieldTypes = getFieldTypes(topic);

        for (QueryRequest.Filter filter : queryRequest.getFilters()) {
            DataFieldType fieldType = fieldTypes.get(filter.getFieldName());
            if (fieldType == null) {
                throw new IllegalArgumentException("Field '" + filter.getFieldName() + "' not found in topic schema");
            }

            validateOperatorForType(filter.getOperator(), fieldType);
        }

        for (QueryRequest.Sort sort : queryRequest.getSorts()) {
            if (!fieldTypes.containsKey(sort.getFieldName())) {
                throw new IllegalArgumentException("Field '" + sort.getFieldName() + "' not found in topic schema");
            }
        }
    }

    private Map<String, DataFieldType> getFieldTypes(DataTopic topic) {
        Map<String, DataFieldType> fieldTypes = new HashMap<>();
        if (topic.getScheme() != null) {
            for (DataTopicField field : topic.getScheme()) {
                fieldTypes.put(field.getName(), field.getType());
            }
        }
        return fieldTypes;
    }

    private void validateOperatorForType(QueryType operator, DataFieldType fieldType) {
        if ((operator == QueryType.MORE || operator == QueryType.LESS) && (fieldType == DataFieldType.STRING || fieldType == DataFieldType.SOURCE)) {
            throw new IllegalArgumentException("Operator " + operator + " cannot be applied to field type " + fieldType);
        }
    }

    private Specification<DataEntity> createTypedSpecification(QueryRequest queryRequest) {
        return Specification.where(DataEntitySpecifications.hasTopicId(queryRequest.getTopicId())).and(DataEntitySpecifications.applyFilters(queryRequest.getFilters()));
    }

    private Pageable createTypedPageable(QueryRequest queryRequest) {
        org.springframework.data.domain.Sort sort = org.springframework.data.domain.Sort.unsorted();

        if (queryRequest.getSorts() != null && !queryRequest.getSorts().isEmpty()) {
            for (QueryRequest.Sort sortObj : queryRequest.getSorts()) {
                org.springframework.data.domain.Sort.Direction direction = sortObj.getDirection() == SortDirection.DESC ? org.springframework.data.domain.Sort.Direction.DESC : org.springframework.data.domain.Sort.Direction.ASC;

                sort = sort.and(org.springframework.data.domain.Sort.by(direction, createSortExpression(sortObj.getFieldName())));
            }
        }

        int page = queryRequest.getPage() != null ? queryRequest.getPage() : 0;
        int size = queryRequest.getSize() != null && queryRequest.getSize() != 0 ? queryRequest.getSize() : Integer.MAX_VALUE;

        return org.springframework.data.domain.PageRequest.of(page, size, sort);
    }

    private String createSortExpression(String fieldName) {
        return "dataFields." + fieldName;
    }

    public List<Map<String, Object>> parse(List<DataEntity> entities) {
        List<Map<String, Object>> result = new ArrayList<>();

        for (DataEntity entity : entities) {
            result.add(parse(entity));
        }

        return result;
    }

    public Map<String, Object> parse(DataEntity entity) {
        Map<String, Object> object = new HashMap<>();

        for (DataField field : entity.getFields()) {
            Object value = convertToTypedValue(field.getDataValue(), field.getType());
            object.put(field.getName(), value);
        }

        return object;
    }

    private Object convertToTypedValue(String value, DataFieldType type) {
        try {
            switch (type) {
                case NUMBER:
                    if (value.contains(".")) {
                        return Float.parseFloat(value);
                    } else {
                        return Integer.parseInt(value);
                    }

                case DATE:
                    return new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH).parse(value);

                default:
                    return value;
            }
        } catch (Exception e) {
            throw new DataParseException(e);
        }
    }
}
