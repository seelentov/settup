package ru.vladislavkomkov.settup.service.impl;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vladislavkomkov.settup.exception.DataParseException;
import ru.vladislavkomkov.settup.exception.DuplicateException;
import ru.vladislavkomkov.settup.exception.NotFoundException;
import ru.vladislavkomkov.settup.model.data.*;
import ru.vladislavkomkov.settup.model.query.*;
import ru.vladislavkomkov.settup.repository.DataEntityRepository;
import ru.vladislavkomkov.settup.repository.DataFieldRepository;
import ru.vladislavkomkov.settup.repository.DataTopicRepository;
import ru.vladislavkomkov.settup.service.DataQueryExecutorService;
import ru.vladislavkomkov.settup.service.DataService;

import static ru.vladislavkomkov.settup.model.data.DataField.DATE_FORMAT;

@Service
public class DataServiceImpl implements DataService {
    private final DataTopicRepository topicRepository;
    private final DataEntityRepository entityRepository;
    private final DataFieldRepository fieldRepository;

    private final DataQueryExecutorService queryExecutor;

    public DataServiceImpl(DataTopicRepository topicRepository, DataEntityRepository entityRepository, DataFieldRepository fieldRepository, DataQueryExecutorService queryExecutor) {
        this.topicRepository = topicRepository;
        this.entityRepository = entityRepository;
        this.fieldRepository = fieldRepository;
        this.queryExecutor = queryExecutor;
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
        if (topicRepository.findByName(topicName).isPresent()) {
            throw new DuplicateException("Topic " + topicName + " already exists");
        }

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

    public List<Map<String, Object>> executeTypedQuery(QueryRequest queryRequest) {
        return queryExecutor.executeQuery(queryRequest);
    }

    public Object executeQuery(QueryRequest queryRequest) {
        List<Map<String, Object>> mapped = executeTypedQuery(queryRequest);
        Object res;
        if (queryRequest.isSingle()) {
            if (mapped.isEmpty()) {
                res = null;
            } else {
                res = mapped.get(0);
            }
        } else {
            res = mapped;
        }

        return res;
    }

    public List<DataTopicField> getTopicScheme(String topicName) {
        Optional<DataTopic> topicOpt = topicRepository.findByName(topicName);

        if (topicOpt.isEmpty()) {
            throw new NotFoundException("Topic " + topicName + " not found");
        }

        return topicOpt.get().getScheme();
    }
}
