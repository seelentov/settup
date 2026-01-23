package ru.vladislavkomkov.settup.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.vladislavkomkov.settup.exception.NotFoundException;
import ru.vladislavkomkov.settup.model.DataEntity;
import ru.vladislavkomkov.settup.model.DataField;
import ru.vladislavkomkov.settup.model.DataTopic;
import ru.vladislavkomkov.settup.model.DataTopicField;
import ru.vladislavkomkov.settup.repository.DataEntityRepository;
import ru.vladislavkomkov.settup.repository.DataFieldRepository;
import ru.vladislavkomkov.settup.repository.DataTopicRepository;
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
    public void addTopic(String topicName, List<DataTopicField> scheme) {
        DataTopic topic = new DataTopic();
        topic.setName(topicName);
        
        for (DataTopicField field : scheme) {
            field.setTopic(topic);
        }
        
        topic.setScheme(scheme);
        topicRepository.save(topic);
    }
    
    @Override
    public void addEntity(DataEntity entity) {
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
        
        Map<String, DataField> fieldMap = existingFields.stream()
                .collect(Collectors.toMap(DataField::getName, Function.identity()));
        
        for (String fieldName : data.keySet()) {
            if (!fieldMap.containsKey(fieldName)) {
                throw new NotFoundException("Field '" + fieldName + "' in entity by id " + id + " not found");
            }
        }
        
        for (var entry : data.entrySet()) {
            DataField field = fieldMap.get(entry.getKey());
            String newValue = entry.getValue();
            if (!Objects.equals(field.getValue(), newValue)) {
                field.setValue(newValue);
            }
        }
    }
}
