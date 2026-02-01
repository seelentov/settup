package ru.vladislavkomkov.settup.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import ru.vladislavkomkov.settup.config.PathMatcherConfig;
import ru.vladislavkomkov.settup.exception.NotFoundException;
import ru.vladislavkomkov.settup.model.page.Page;
import ru.vladislavkomkov.settup.model.page.PageMatch;
import ru.vladislavkomkov.settup.repository.PageRepository;
import ru.vladislavkomkov.settup.service.PageService;

@Service
public class PageServiceImpl implements PageService {
    private final PageRepository repository;
    private final PathMatcher pathMatcher;

    public PageServiceImpl(
            PageRepository repository,
            @PathMatcherConfig.AntPathMatcherQualifier PathMatcher pathMatcher
    ) {
        this.repository = repository;
        this.pathMatcher = pathMatcher;
    }

    @Override
    public List<Page> getPages() {
        return repository.findByIsActiveTrue();
    }

    public Optional<PageMatch> findPageWithVariables(String requestUri) {
        // Нормализуем URI: убираем завершающий слэш, кроме корня
        String normalizedRequest = normalizePath(requestUri);

        // Получаем все активные страницы (можно кэшировать!)
        List<Page> pages = repository.findByIsActiveTrue();

        for (Page page : pages) {
            String pagePattern = normalizePath(page.getUrl());

            if (normalizedRequest.equals(pagePattern)) {
                return Optional.of(new PageMatch(page, Collections.emptyMap()));
            }

            if (pathMatcher.match(pagePattern, normalizedRequest)) {
                Map<String, String> variables = pathMatcher.extractUriTemplateVariables(pagePattern, normalizedRequest);
                return Optional.of(new PageMatch(page, variables));
            }
        }

        return Optional.empty();
    }

    private String normalizePath(String path) {
        if (path == null || path.isEmpty()) {
            return "/";
        }
        // Приводим к виду без завершающего слэша, кроме корня
        if (path.length() > 1 && path.endsWith("/")) {
            return path.substring(0, path.length() - 1);
        }
        return path;
    }

    @Override
    public Optional<Page> getPage(String url) {
        return findPageWithVariables(url).map(PageMatch::getPage);
    }

    @Override
    public void addPage(Page page) {
        repository.save(page);
    }

    @Override
    public void editPage(int id, Page newData) {
        Page existing = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Page not found: " + id));

        existing.setUrl(newData.getUrl());
        existing.setTemplateLink(newData.getTemplateLink());
        existing.setActive(newData.isActive());
        repository.save(existing);
    }
}
