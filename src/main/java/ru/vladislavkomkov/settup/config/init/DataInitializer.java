package ru.vladislavkomkov.settup.config.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import ru.vladislavkomkov.settup.exception.DuplicateException;
import ru.vladislavkomkov.settup.model.data.DataField;
import ru.vladislavkomkov.settup.model.data.DataFieldType;
import ru.vladislavkomkov.settup.model.data.DataTopic;
import ru.vladislavkomkov.settup.model.data.DataTopicField;
import ru.vladislavkomkov.settup.service.DataService;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    private DataService dataService;

    public DataInitializer(DataService dataService) {
        this.dataService = dataService;
    }

    @Override
    public void run(String... args) throws Exception {
    }
}
