package ru.vladislavkomkov.settup.model.data;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "data_topics")
public class DataTopic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private boolean isActive = true;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DataEntity> entities;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DataTopicField> scheme;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DataEntity> getEntities() {
        return entities;
    }

    public void setEntities(List<DataEntity> entities) {
        this.entities = entities;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public List<DataTopicField> getScheme() {
        return scheme;
    }

    public void setScheme(List<DataTopicField> scheme) {
        this.scheme = scheme;
    }
}