package ru.vladislavkomkov.settup.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import ru.vladislavkomkov.settup.model.data.DataEntity;
import ru.vladislavkomkov.settup.model.data.DataField;
import ru.vladislavkomkov.settup.model.data.DataTopic;
import ru.vladislavkomkov.settup.model.data.DataTopicField;
import ru.vladislavkomkov.settup.model.query.Query;
import ru.vladislavkomkov.settup.model.query.QueryRequest;

public interface DataService {
    List<DataTopic> getTopics();

    List<DataEntity> getTopic(String topicName);

    Optional<DataEntity> getEntity(int id);

    DataTopic addTopic(String topicName, List<DataTopicField> scheme);

    void addEntity(String topicName, List<DataField> dataFields);

    void editEntity(int id, Map<String, String> data);

    Page<DataEntity> executeTypedQuery(QueryRequest queryRequest);

    List<Map<String, Object>> parse(List<DataEntity> entities);

    Map<String, Object> parse(DataEntity entity);
}
