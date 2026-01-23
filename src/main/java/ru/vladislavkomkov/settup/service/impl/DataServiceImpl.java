package ru.vladislavkomkov.settup.service.impl;

import org.springframework.stereotype.Service;

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
}
