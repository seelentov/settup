package ru.vladislavkomkov.settup.service;

import java.util.List;

import ru.vladislavkomkov.settup.model.DataEntity;
import ru.vladislavkomkov.settup.model.DataField;
import ru.vladislavkomkov.settup.model.DataTopic;

public interface DataService {
    List<DataTopic> getTopics();
    
    List<DataEntity> getTopic(String topicName);
    
    DataEntity getEntity(String topicName, int id);
    
    void addTopic(String topicName, List<DataField> scheme);
    
    void addEntity(String topicName, DataEntity entity);
}
