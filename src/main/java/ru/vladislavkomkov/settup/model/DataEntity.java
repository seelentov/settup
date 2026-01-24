package ru.vladislavkomkov.settup.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "data_entities")
public class DataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @OneToMany(mappedBy = "entity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DataField> dataFields;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "data_topic_id")
    private DataTopic topic;
    
    private boolean isActive = true;
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public List<DataField> getFields() {
        return dataFields;
    }
    
    public void setFields(List<DataField> dataFields) {
        this.dataFields = dataFields;
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
}