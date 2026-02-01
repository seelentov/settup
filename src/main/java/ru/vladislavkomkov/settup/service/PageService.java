package ru.vladislavkomkov.settup.service;

import java.util.List;
import java.util.Optional;

import ru.vladislavkomkov.settup.model.page.Page;
import ru.vladislavkomkov.settup.model.page.PageMatch;

public interface PageService {
    List<Page> getPages();

    Optional<Page> getPage(String url);

    Optional<PageMatch> findPageWithVariables(String requestUri);

    void addPage(Page page);

    void editPage(int id, Page page);
}
