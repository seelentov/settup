package ru.vladislavkomkov.settup.service.data.impl;

import java.util.*;

import org.springframework.stereotype.Service;
import ru.vladislavkomkov.settup.exception.data.DuplicateException;
import ru.vladislavkomkov.settup.exception.NotFoundException;
import ru.vladislavkomkov.settup.model.data.*;
import ru.vladislavkomkov.settup.model.query.*;
import ru.vladislavkomkov.settup.repository.data.DataEntityRepository;
import ru.vladislavkomkov.settup.repository.data.DataTopicRepository;
import ru.vladislavkomkov.settup.service.data.DataQueryExecutorService;
import ru.vladislavkomkov.settup.service.data.DataService;

@Service
public class DataServiceImpl implements DataService {
    private final DataTopicRepository topicRepository;
    private final DataEntityRepository entityRepository;

    private final DataQueryExecutorService queryExecutor;

    public DataServiceImpl(DataTopicRepository topicRepository, DataEntityRepository entityRepository, DataQueryExecutorService queryExecutor) {
        this.topicRepository = topicRepository;
        this.entityRepository = entityRepository;
        this.queryExecutor = queryExecutor;
    }

    @Override
    public List<DataTopic> getTopics() {
        return topicRepository.findAll();
    }

    @Override
    public Optional<DataTopic> getTopic(String topicName) {
        return topicRepository.findByName(topicName);
    }

    @Override
    public List<DataEntity> getTopicData(String topicName) {
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
    public void editEntity(int id, Map<String, String> data) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public List<Map<String, Object>> executeTypedQuery(QueryRequest queryRequest) {
        return queryExecutor.executeQuery(queryRequest);
    }

    @Override
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

    @Override
    public List<DataTopicField> getTopicScheme(String topicName) {
        Optional<DataTopic> topicOpt = topicRepository.findByName(topicName);

        if (topicOpt.isEmpty()) {
            throw new NotFoundException("Topic " + topicName + " not found");
        }

        return topicOpt.get().getScheme();
    }
}
