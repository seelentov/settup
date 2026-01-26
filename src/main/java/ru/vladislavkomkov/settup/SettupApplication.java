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
        SpringApplication.run(SettupApplication.class, args);
    }
}
