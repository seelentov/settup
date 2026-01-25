package ru.vladislavkomkov.settup.service.impl;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import ru.vladislavkomkov.settup.exception.NotAllowedException;
import ru.vladislavkomkov.settup.exception.StaticException;
import ru.vladislavkomkov.settup.model.Static;
import ru.vladislavkomkov.settup.service.StaticService;

@Service
public class StaticServiceImpl implements StaticService {
    @Override
    public List<Static> getStatics(String folderPath) {
        Path path = Path.of(folderPath);
        if(path.equals(ADMIN_FOLDER_PATH)){
            throw new NotAllowedException("Not allowed admin files edit");
        }

        try {
            Path folder = path.toAbsolutePath().normalize();
            if (!Files.exists(folder)) {
                throw new StaticException("Folder not exists: " + folderPath);
            }
            if (!Files.isDirectory(folder)) {
                throw new StaticException("Path is not a directory: " + folderPath);
            }
            
            List<Static> statics = new ArrayList<>();
            try (Stream<Path> paths = Files.list(folder)) {
                paths.filter(Files::isRegularFile)
                        .forEach(file -> {
                            String filename = file.getFileName().toString();
                            statics.add(new Static(filename, file.toString(), null));
                        });
            }
            return statics;
        } catch (IOException ex) {
            throw new StaticException("Failed to list files in folder: " + folderPath, ex);
        }
    }
    
    @Override
    public Static getStatic(String filePath) {
        try {
            Path file = Path.of(filePath).toAbsolutePath().normalize();
            if (!Files.exists(file)) {
                throw new StaticException("File not found: " + filePath);
            }
            if (!Files.isRegularFile(file)) {
                throw new StaticException("Path is not a file: " + filePath);
            }
            
            String content = Files.readString(file, StandardCharsets.UTF_8);
            String filename = file.getFileName().toString();
            return new Static(filename, file.toString(), content);
        } catch (IOException ex) {
            throw new StaticException("Failed to read file: " + filePath, ex);
        }
    }
    
    @Override
    public void addStatic(String folderPath, String filename, String content) {
        try {
            Path folder = Path.of(folderPath).toAbsolutePath().normalize();
            Path file = folder.resolve(filename).toAbsolutePath().normalize();
            
            if (!file.startsWith(folder)) {
                throw new StaticException("Invalid filename: possible path traversal attempt");
            }
            
            Files.createDirectories(folder);
            Files.writeString(file, content, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        } catch (Exception ex) {
            throw new StaticException("Can't create " + folderPath + "/" + filename, ex);
        }
    }
}