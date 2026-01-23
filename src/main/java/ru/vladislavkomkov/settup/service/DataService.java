package ru.vladislavkomkov.settup.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import ru.vladislavkomkov.settup.model.DataEntity;
import ru.vladislavkomkov.settup.model.DataTopic;
import ru.vladislavkomkov.settup.model.DataTopicField;

public interface DataService {
    List<DataTopic> getTopics();
    
    List<DataEntity> getTopic(String topicName);
    
    Optional<DataEntity> getEntity(int id);
    
    void addTopic(String topicName, List<DataTopicField> scheme);
    
    void addEntity(DataEntity entity);
    
    void editEntity(int id, Map<String, String> data);
}
