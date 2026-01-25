package ru.vladislavkomkov.settup.model.data;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "data_topic_fields")
public class DataTopicField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private DataFieldType type;

    private String defaultValue;

    private boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "data_topic_id")
    private DataTopic topic;

    public DataTopicField() {
    }

    public DataTopicField(String name, DataFieldType type) {
        this.name = name;
        this.type = type;
    }

    public DataTopic getTopic() {
        return topic;
    }

    public void setTopic(DataTopic topic) {
        this.topic = topic;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public DataFieldType getType() {
        return type;
    }

    public void setType(DataFieldType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
