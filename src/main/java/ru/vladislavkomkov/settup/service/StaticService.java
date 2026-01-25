package ru.vladislavkomkov.settup.service;

import java.nio.file.Path;
import java.util.List;

import ru.vladislavkomkov.settup.model.Static;

public interface StaticService {
    public static final Path STATIC_FOLDER_PATH = Path.of("static");

    public static final Path ADMIN_FOLDER_PATH = Path.of(String.valueOf(STATIC_FOLDER_PATH), "admin");

    List<Static> getStatics(String folderPath);
    
    Static getStatic(String filePath);
    
    void addStatic(String folderPath, String filename, String content);
}
