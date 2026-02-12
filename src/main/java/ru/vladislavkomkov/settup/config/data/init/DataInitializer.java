package ru.vladislavkomkov.settup.config.data.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import ru.vladislavkomkov.settup.service.data.DataService;

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
