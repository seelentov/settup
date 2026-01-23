package ru.vladislavkomkov.settup.config.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import ru.vladislavkomkov.settup.model.Page;
import ru.vladislavkomkov.settup.service.PageService;

@Component
public class PageInitializer implements CommandLineRunner {
    private PageService pageService;
    
    public PageInitializer(PageService pageService) {
        this.pageService = pageService;
    }
    
    @Override
    public void run(String... args) throws Exception {
        if (pageService.getPage(PageService.HOME_PAGE_PATH).isEmpty()) {
            
            
            pageService.addPage(new Page(PageService.HOME_PAGE_PATH, PageService.HOME_PAGE_TEMPLATE));
        }
    }
}
