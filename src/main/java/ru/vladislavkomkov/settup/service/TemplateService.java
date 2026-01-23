package ru.vladislavkomkov.settup.service;

import java.util.List;
import java.util.Optional;

import ru.vladislavkomkov.settup.model.Template;

public interface TemplateService {
    List<Template> getTemplates();
    
    Optional<String> getTemplate(String name);
    
    void addTemplate(String name, String content);
    
    void editTemplate(String name, String content);
    
    void deleteTemplate(String name);
}
