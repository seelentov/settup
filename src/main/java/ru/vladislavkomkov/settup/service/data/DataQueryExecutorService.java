package ru.vladislavkomkov.settup.service.data;

import ru.vladislavkomkov.settup.model.query.QueryRequest;

import java.util.List;
import java.util.Map;

public interface DataQueryExecutorService {
    List<Map<String, Object>> executeQuery(QueryRequest request);
}
