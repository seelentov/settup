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
    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private DataService dataService;

    public DataInitializer(DataService dataService) {
        this.dataService = dataService;
    }

    @Override
    public void run(String... args) throws Exception {
        String testTopicName = "test";

        String stringFieldName = "string";
        String sourceFieldName = "source";
        String dateFieldName = "date";
        String numberFieldName = "number";

        try {
            List<DataTopicField> fields = List.of(
                    new DataTopicField(stringFieldName, DataFieldType.STRING),
                    new DataTopicField(sourceFieldName, DataFieldType.SOURCE),
                    new DataTopicField(dateFieldName, DataFieldType.DATE),
                    new DataTopicField(numberFieldName, DataFieldType.NUMBER)
            );

            dataService.addTopic(testTopicName, fields);

            for (int i = 0; i < 100; i++) {
                dataService.addEntity(testTopicName, List.of(
                        new DataField(stringFieldName, DataFieldType.STRING, "test" + i),
                        new DataField(sourceFieldName, DataFieldType.SOURCE, "/static/admin/admin.js"),
                        new DataField(dateFieldName, DataFieldType.DATE, new Date(i * Instant.now().toEpochMilli()).toString()),
                        new DataField(numberFieldName, DataFieldType.NUMBER, "" + i))
                );
            }
        } catch (DuplicateException ex) {
            log.debug("Topic {} already exist. Skip creation", testTopicName);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
