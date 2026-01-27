package ru.vladislavkomkov.settup.config.init;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import ru.vladislavkomkov.settup.model.Page;
import ru.vladislavkomkov.settup.model.data.*;
import ru.vladislavkomkov.settup.model.query.*;
import ru.vladislavkomkov.settup.service.DataService;
import ru.vladislavkomkov.settup.service.PageService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class PageInitializer implements CommandLineRunner {
    @Value("${spring.application.mode}")
    private String appMode;

    private final PageService pageService;
    private final DataService dataService;

    public PageInitializer(PageService pageService, DataService dataService) {
        this.pageService = pageService;
        this.dataService = dataService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (appMode.equals("development") || pageService.getPage(Page.HOME_PAGE_PATH).isEmpty()) {
            Page page = new Page(Page.HOME_PAGE_PATH, Page.HOME_PAGE_TEMPLATE);

            if (appMode.equals("development")) {
                List<DataTopicField> fields = List.of(new DataTopicField("string", DataFieldType.STRING), new DataTopicField("source", DataFieldType.SOURCE), new DataTopicField("date", DataFieldType.DATE), new DataTopicField("number", DataFieldType.NUMBER));

                DataTopic testTopic = dataService.addTopic("test", fields);

                for (int i = 0; i < 100; i++) {
                    dataService.addEntity("test", List.of(new DataField("string", DataFieldType.STRING, "test" + i), new DataField("source", DataFieldType.SOURCE, "/static/admin/admin.js"), new DataField("date", DataFieldType.DATE, new Date(i * Instant.now().toEpochMilli()).toString()), new DataField("number", DataFieldType.NUMBER, "" + i)));
                }

                List<Query> queries = new ArrayList<>();
                Query query = new Query();
                query.setPage(page);
                query.setName("test0");
                query.setTopic(testTopic);

                List<QueryRow> rows = List.of(
                        new QueryRow("string", QueryType.CONTAINS, "test0"),
                        new QueryRow("source", QueryType.CONTAINS, "/static/admin/admin.js"),
                        new QueryRow("date", QueryType.CONTAINS, new Date(0).toString()),
                        new QueryRow("number", QueryType.CONTAINS, "0")
                );

                for (QueryRow row : rows) {
                    row.setQuery(query);
                }

                query.setQuery(rows);
                query.setPageSize(2);
                queries.add(query);

                Query query2 = new Query();
                query2.setPage(page);
                query2.setName("test1");
                query2.setTopic(testTopic);

                List<QueryRow> rows2 = List.of(
                        new QueryRow("string", QueryType.EQUALS, "test10")
                );

                for (QueryRow row : rows2) {
                    row.setQuery(query2);
                }

                query2.setQuery(rows2);
                query2.setPageCount(0);
                query2.setPageSize(1);
                queries.add(query2);

                Query query3 = new Query();
                query3.setPage(page);
                query3.setName("test2");
                query3.setTopic(testTopic);

                List<QueryRow> rows3 = List.of(
                        new QueryRow("date", QueryType.LESS, "Fri Jan 16 11:00:50 YEKT 4381"),
                        new QueryRow("date", QueryType.MORE, "Fri Jun 24 01:31:14 YEKT 3932")
                );

                for (QueryRow row : rows3) {
                    row.setQuery(query3);
                }

                query3.setQuery(rows3);
                query3.setPageCount(0);
                query3.setPageSize(1);
                queries.add(query3);

                Query query4 = new Query();
                query4.setPage(page);
                query4.setName("test3");
                query4.setTopic(testTopic);

                List<QuerySort> rows4 = List.of(
                        new QuerySort("date", SortDirection.DESC)
                );

                for (QuerySort row : rows4) {
                    row.setQuery(query4);
                }

                query4.setSorts(rows4);
                query4.setPageCount(0);
                query4.setPageSize(10);
                queries.add(query4);

                page.setQueries(queries);

                page.setTemplateLink(Page.HOME_PAGE_TEMPLATE + "_test");
            }

            pageService.addPage(page);
        }
    }
}
