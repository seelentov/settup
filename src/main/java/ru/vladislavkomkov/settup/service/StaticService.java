package ru.vladislavkomkov.settup.service;

import java.util.List;

import ru.vladislavkomkov.settup.model.Static;

public interface StaticService {
    List<Static> getStatics(String folderPath);
    
    Static getStatic(String filePath);
    
    void addStatic(String filePath, String content);
}
