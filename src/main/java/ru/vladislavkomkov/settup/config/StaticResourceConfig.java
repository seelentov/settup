package ru.vladislavkomkov.settup.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

import ru.vladislavkomkov.settup.service.sttc.StaticService;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path staticDir = Paths.get("./" + StaticService.STATIC_FOLDER_PATH).toAbsolutePath().normalize();
        String staticDirPath = staticDir.toUri().toString();
        
        registry.addResourceHandler("/" + StaticService.STATIC_FOLDER_PATH + "/**")
                .addResourceLocations(staticDirPath)
                .setCachePeriod(0);
    }
}