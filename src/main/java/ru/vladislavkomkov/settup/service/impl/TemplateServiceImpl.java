package ru.vladislavkomkov.settup.service.impl;

import ru.vladislavkomkov.settup.model.Template;
import ru.vladislavkomkov.settup.service.TemplateService;

import java.util.List;
import java.util.Optional;

public class TemplateServiceImpl implements TemplateService {
    @Override
    public List<Template> getTemplates() {
        return List.of();
    }

    @Override
    public Optional<String> getTemplate(String name) {
        return Optional.empty();
    }

    @Override
    public void addTemplate(String name, String content) {

    }

    @Override
    public void editTemplate(String name, String content) {

    }

    @Override
    public void deleteTemplate(String name) {

    }
}
