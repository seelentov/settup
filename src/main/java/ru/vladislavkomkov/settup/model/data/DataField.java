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
@Table(name = "data_fields")
public class DataField {
    public static final String DATE_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String dataValue;

    private DataFieldType type;

    private boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "data_entity_id")
    private DataEntity entity;

    public DataField(){}

    public DataField(String name, DataFieldType type, String dataValue) {
        this.name = name;
        this.type = type;
        this.dataValue = dataValue;
    }

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

    public String getDataValue() {
        return dataValue;
    }

    public void setDataValue(String dataValue) {
        this.dataValue = dataValue;
    }

    public DataEntity getEntity() {
        return entity;
    }

    public void setEntity(DataEntity entity) {
        this.entity = entity;
    }

    public DataFieldType getType() {
        return type;
    }

    public void setType(DataFieldType type) {
        this.type = type;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}