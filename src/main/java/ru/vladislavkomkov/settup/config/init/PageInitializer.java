package ru.vladislavkomkov.settup.config.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final PageService pageService;
    private final DataService dataService;

    public PageInitializer(PageService pageService, DataService dataService) {
        this.pageService = pageService;
        this.dataService = dataService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (pageService.getPage(Page.HOME_PAGE_PATH).isEmpty()) {
            Page page = new Page(Page.HOME_PAGE_PATH, Page.HOME_PAGE_TEMPLATE);
            pageService.addPage(page);
        }

        if (pageService.getPage("test").isEmpty()) {
            Page page = new Page("test", "test");
            pageService.addPage(page);
        }

    }
}
