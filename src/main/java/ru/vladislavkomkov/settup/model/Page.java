package ru.vladislavkomkov.settup.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "pages", uniqueConstraints = @UniqueConstraint(columnNames = "url"))
public class Page {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String templateLink;
    private String url;
    
    private boolean isActive = true;
    
    public Page(String url, String templateLink) {
        this.url = url;
        this.templateLink = templateLink;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    public String getTemplateLink() {
        return templateLink;
    }
    
    public void setTemplateLink(String templateLink) {
        this.templateLink = templateLink;
    }
}
