package ru.vladislavkomkov.settup.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.vladislavkomkov.settup.exception.NotFoundException;
import ru.vladislavkomkov.settup.model.Page;
import ru.vladislavkomkov.settup.service.PageService;

import java.util.Optional;

@Controller
public class PagesController {
    private final PageService service;

    public PagesController(PageService service) {
        this.service = service;
    }

    @GetMapping
    public String index() {
        Optional<Page> indexPage = service.getPage(Page.HOME_PAGE_PATH);
        if (indexPage.isEmpty()) {
            throw new NotFoundException("Page '' not found");
        }

        return indexPage.get().getTemplateLink();
    }
}
