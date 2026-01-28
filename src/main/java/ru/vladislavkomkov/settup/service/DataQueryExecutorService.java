package ru.vladislavkomkov.settup.service;

import ru.vladislavkomkov.settup.model.query.QueryRequest;

import java.util.List;
import java.util.Map;

public interface DataQueryExecutorService {
    List<Map<String, Object>> executeQuery(QueryRequest request);
}
