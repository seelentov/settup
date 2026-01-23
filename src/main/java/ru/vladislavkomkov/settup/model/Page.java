package ru.vladislavkomkov.settup.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "pages")
public class Page {
    private String template_link;
    private String url;
    
    private boolean isActive;
    
    public Page(String url, String template_link) {
        this.url = url;
        this.template_link = template_link;
    }
}
