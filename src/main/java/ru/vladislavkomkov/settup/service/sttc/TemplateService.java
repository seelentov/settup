package ru.vladislavkomkov.settup.service.sttc;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import ru.vladislavkomkov.settup.model.sttc.Template;

public interface TemplateService {
    Path TEMPLATES_FOLDER_PATH = Path.of("templates");

    Path ADMIN_FOLDER_PATH = Path.of(String.valueOf(TEMPLATES_FOLDER_PATH), "admin");

    List<Template> getTemplates();

    Optional<String> getTemplate(String name);

    void addTemplate(String name, String content);

    void editTemplate(String name, String content);

    void deleteTemplate(String name);
}
