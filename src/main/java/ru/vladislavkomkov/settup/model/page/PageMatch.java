package ru.vladislavkomkov.settup.model.page;

import java.util.Collections;
import java.util.Map;

public class PageMatch {
    private final Page page;
    private final Map<String, String> pathVariables;

    public PageMatch(Page page, Map<String, String> pathVariables) {
        this.page = page;
        this.pathVariables = Collections.unmodifiableMap(pathVariables);
    }

    public Page getPage() {
        return page;
    }

    public Map<String, String> getPathVariables() {
        return pathVariables;
    }
}
