package ru.vladislavkomkov.settup.service.data;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import ru.vladislavkomkov.settup.model.data.DataEntity;
import ru.vladislavkomkov.settup.model.data.DataField;
import ru.vladislavkomkov.settup.model.data.DataTopic;
import ru.vladislavkomkov.settup.model.data.DataTopicField;
import ru.vladislavkomkov.settup.model.query.QueryRequest;

public interface DataService {
    List<DataTopic> getTopics();

    Optional<DataTopic> getTopic(String topicName);

    List<DataEntity> getTopicData(String topicName);

    List<DataTopicField> getTopicScheme(String topicName);

    Optional<DataEntity> getEntity(int id);

    DataTopic addTopic(String topicName, List<DataTopicField> scheme);

    void addEntity(String topicName, List<DataField> dataFields);

    void editEntity(int id, Map<String, String> data);

    List<Map<String, Object>> executeTypedQuery(QueryRequest queryRequest);

    Object executeQuery(QueryRequest query);
}
