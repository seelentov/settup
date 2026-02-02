package ru.vladislavkomkov.settup.config.data.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import ru.vladislavkomkov.settup.exception.data.DuplicateException;
import ru.vladislavkomkov.settup.model.data.DataField;
import ru.vladislavkomkov.settup.model.data.DataFieldType;
import ru.vladislavkomkov.settup.model.data.DataTopic;
import ru.vladislavkomkov.settup.model.data.DataTopicField;
import ru.vladislavkomkov.settup.model.page.Page;
import ru.vladislavkomkov.settup.model.query.Query;
import ru.vladislavkomkov.settup.model.query.QueryRow;
import ru.vladislavkomkov.settup.model.query.QueryType;
import ru.vladislavkomkov.settup.service.data.DataService;
import ru.vladislavkomkov.settup.service.page.PageService;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Component
public class PageInitializer implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(PageInitializer.class);

    private final PageService pageService;
    private final DataService dataService;

    public PageInitializer(PageService pageService, DataService dataService) {
        this.pageService = pageService;
        this.dataService = dataService;
    }

    @Override
    public void run(String... args) throws Exception {
        setupData();
        setupPage();
    }

    private void setupPage() {
        Page page = new Page("", Page.HOME_PAGE_TEMPLATE);

        String testTopicName = "test";
        String testQuery = "query";

        Query query = new Query();
        page.setQueries(List.of(query));
        DataTopic topic = dataService.getTopic(testTopicName).get();
        query.setTopic(topic);

        query.setName(testQuery);
        query.setPage(page);

        QueryRow row = new QueryRow("{key}", QueryType.EQUALS, "{value}");

        row.setQuery(query);
        query.getQuery().add(row);

        pageService.addPage(page);
    }

    private void setupData() {
        String testTopicName = "test";

        String stringFieldName = "string";
        String sourceFieldName = "source";
        String dateFieldName = "date";
        String numberFieldName = "number";

        try {
            List<DataTopicField> fields = List.of(new DataTopicField(stringFieldName, DataFieldType.STRING), new DataTopicField(sourceFieldName, DataFieldType.SOURCE), new DataTopicField(dateFieldName, DataFieldType.DATE), new DataTopicField(numberFieldName, DataFieldType.NUMBER));

            dataService.addTopic(testTopicName, fields);

            for (int i = 0; i < 100; i++) {
                dataService.addEntity(testTopicName, List.of(new DataField(stringFieldName, DataFieldType.STRING, "test" + i), new DataField(sourceFieldName, DataFieldType.SOURCE, "/static/admin/admin.js"), new DataField(dateFieldName, DataFieldType.DATE, new Date(i * Instant.now().toEpochMilli()).toString()), new DataField(numberFieldName, DataFieldType.NUMBER, "" + i)));
            }
        } catch (DuplicateException ex) {
            log.debug(String.valueOf(ex));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
