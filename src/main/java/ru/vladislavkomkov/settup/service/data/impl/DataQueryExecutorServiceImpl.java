package ru.vladislavkomkov.settup.service.data.impl;

import org.springframework.stereotype.Service;
import ru.vladislavkomkov.settup.model.query.QueryRequest;
import ru.vladislavkomkov.settup.service.data.DataQueryExecutorService;

import java.util.*;

@Service
public class DataQueryExecutorServiceImpl implements DataQueryExecutorService {

    @Override
    public List<Map<String, Object>> executeQuery(QueryRequest request) {
        throw new RuntimeException("Not implemented");
    }
}