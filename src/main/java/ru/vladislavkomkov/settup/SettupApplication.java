package ru.vladislavkomkov.settup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class SettupApplication {
    public static void main(String[] args) {
        Path templatesDir = Paths.get("./templates").toAbsolutePath().normalize();
        System.out.println("Проверка директории шаблонов: " + templatesDir);

        if (Files.exists(templatesDir)) {
            if (Files.isDirectory(templatesDir)) {
                System.out.println("Директория существует. Содержимое:");
                try {
                    Files.list(templatesDir)
                            .map(Path::getFileName)
                            .forEach(System.out::println);
                } catch (IOException e) {
                    System.err.println("Ошибка при чтении директории: " + e.getMessage());
                }
            } else {
                System.out.println("Путь существует, но это НЕ директория.");
            }
        } else {
            System.out.println("Директория ./templates НЕ существует.");
        }
        SpringApplication.run(SettupApplication.class, args);
    }
}
