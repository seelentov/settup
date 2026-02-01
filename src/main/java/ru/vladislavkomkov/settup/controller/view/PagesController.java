package ru.vladislavkomkov.settup.controller.view;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.vladislavkomkov.settup.exception.NotFoundException;
import ru.vladislavkomkov.settup.model.page.Page;
import ru.vladislavkomkov.settup.model.page.PageMatch;
import ru.vladislavkomkov.settup.model.query.Query;
import ru.vladislavkomkov.settup.model.query.QueryRequest;
import ru.vladislavkomkov.settup.service.DataService;
import ru.vladislavkomkov.settup.service.PageService;

import java.util.HashMap;
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

    @GetMapping("/")
    public String index(@RequestParam Map<String, String> allParams, Model model) {
        return renderPage("", allParams, model);
    }

    @GetMapping("/**")
    public String page(HttpServletRequest request,
                       @RequestParam Map<String, String> allParams,
                       Model model) {
        String requestUri = request.getRequestURI();
        String contextPath = request.getContextPath();
        if (contextPath != null && !contextPath.isEmpty()) {
            requestUri = requestUri.substring(contextPath.length());
        }
        return renderPage(requestUri, allParams, model);
    }

    private String renderPage(String requestUri, Map<String, String> queryParams, Model model) {
        Optional<PageMatch> matchOpt = pageService.findPageWithVariables(requestUri);
        if (matchOpt.isEmpty()) {
            throw new NotFoundException("Page '" + requestUri + "' not found");
        }

        PageMatch match = matchOpt.get();
        Page page = match.getPage();
        Map<String, String> pathVariables = match.getPathVariables();

        Map<String, String> allVariables = new HashMap<>(queryParams);
        allVariables.putAll(pathVariables);

        model.addAllAttributes(allVariables);

        for (Query query : page.getQueries()) {
            QueryRequest originalRequest = query.toQueryRequest();
            QueryRequest resolvedRequest = originalRequest.resolveVariables(allVariables);
            Object data = dataService.executeQuery(resolvedRequest);
            model.addAttribute(query.getName(), data);
        }

        return page.getTemplateLink();
    }
}