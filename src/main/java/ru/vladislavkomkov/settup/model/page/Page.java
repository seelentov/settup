package ru.vladislavkomkov.settup.model.page;

import java.util.List;

import jakarta.persistence.*;
import ru.vladislavkomkov.settup.model.query.Query;

@Entity
@Table(name = "pages", uniqueConstraints = @UniqueConstraint(columnNames = "url"))
public class Page {
    public static final String HOME_PAGE_PATH = "";
    public static final String HOME_PAGE_TEMPLATE = "index";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String templateLink;
    private String url;

    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Query> queries;

    private boolean isActive = true;

    public Page(){

    }

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

    public List<Query> getQueries() {
        return queries;
    }

    public void setQueries(List<Query> queries) {
        this.queries = queries;
    }
}
