package ru.vladislavkomkov.settup.service;

import java.nio.file.Path;
import java.util.List;

import ru.vladislavkomkov.settup.model.Static;

public interface StaticService {
    public static final Path STATIC_FOLDER_PATH = Path.of("./static");
    public static final Path JS_FOLDER_PATH = Path.of(String.valueOf(STATIC_FOLDER_PATH), "js");
    public static final Path CSS_FOLDER_PATH = Path.of(String.valueOf(STATIC_FOLDER_PATH), "css");
    
    public static final Path JS_ADMIN_FOLDER_PATH = Path.of(String.valueOf(STATIC_FOLDER_PATH), "admin", "js");
    public static final Path CSS_ADMIN_FOLDER_PATH = Path.of(String.valueOf(STATIC_FOLDER_PATH), "admin", "css");
    
    List<Static> getStatics(String folderPath);
    
    Static getStatic(String filePath);
    
    void addStatic(String folderPath, String filename, String content);
}
