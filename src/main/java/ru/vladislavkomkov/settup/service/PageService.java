package ru.vladislavkomkov.settup.service;

import java.util.List;
import java.util.Optional;

import ru.vladislavkomkov.settup.model.Page;

public interface PageService {
    public static final String HOME_PAGE_PATH = "/";
    public static final String HOME_PAGE_TEMPLATE = "index.html";
    
    List<Page> getPages();
    
    Optional<Page> getPage(String url);
    
    void addPage(Page page);
    
    void editPage(int id, Page page);
}
