package ru.vladislavkomkov.settup.controller.view;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.vladislavkomkov.settup.exception.NotFoundException;
import ru.vladislavkomkov.settup.model.Page;
import ru.vladislavkomkov.settup.model.data.DataEntity;
import ru.vladislavkomkov.settup.model.query.Query;
import ru.vladislavkomkov.settup.service.DataService;
import ru.vladislavkomkov.settup.service.PageService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class PagesController {
    private final PageService pageService;
    private final DataService dataService;

    public PagesController(PageService pageService, DataService dataService) {
        this.pageService = pageService;
        this.dataService = dataService;
    }

    @GetMapping
    public String index(Model model) {
        Optional<Page> indexPage = pageService.getPage(Page.HOME_PAGE_PATH);
        if (indexPage.isEmpty()) {
            throw new NotFoundException("Page '' not found");
        }

        for (Query query : indexPage.get().getQueries()) {
            org.springframework.data.domain.Page<DataEntity> data = dataService.executeTypedQuery(query.toQueryRequest());
            List<Map<String, Object>> mapped = dataService.parse(data.stream().toList());
            Object res;
            if (query.isSingle()) {
                if (mapped.isEmpty()) {
                    res = null;
                } else {
                    res = mapped.get(0);
                }
            } else {
                res = mapped;
            }

            model.addAttribute(query.getName(), res);
        }

        return indexPage.get().getTemplateLink();
    }
}
