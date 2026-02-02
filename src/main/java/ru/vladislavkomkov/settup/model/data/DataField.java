package ru.vladislavkomkov.settup.model.data;

import jakarta.persistence.*;

import java.math.BigDecimal; // Используем BigDecimal для чисел, если нужна высокая точность
import java.time.LocalDateTime; // Или java.util.Date/java.sql.Timestamp

@Entity
@Table(name = "data_fields")
public class DataField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Enumerated(EnumType.STRING)
    private DataFieldType type;

    private String stringValue;
    private Long numberValue;

    private boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "data_entity_id")
    private DataEntity entity;

    public DataField() {
    }

    public DataField(String name, DataFieldType type, Object rawValue) {
        this.name = name;
        this.type = type;
        setValue(rawValue);
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
        this.isActive = active;
    }

    public DataEntity getEntity() {
        return entity;
    }

    public void setEntity(DataEntity entity) {
        this.entity = entity;
    }

    public String getStringValue() {
        return stringValue;
    }

    public Long getNumberValue() {
        return numberValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public void setNumberValue(Long numberValue) {
        this.numberValue = numberValue;
    }

    public void setValue(Object value) {
        switch (this.type) {
            case STRING:
            case SOURCE:
                this.stringValue = value instanceof String ? (String) value : (value != null ? value.toString() : null);
                break;
            case NUMBER:
            case DATE:
                if (value instanceof Number) {
                    this.numberValue = ((Number) value).longValue();
                } else if (value instanceof String) {
                    try {
                        this.numberValue = Long.parseLong((String) value);
                    } catch (NumberFormatException e) {
                        this.numberValue = null;
                    }
                } else {
                    this.numberValue = null;
                }
                break;
            default:
                throw new IllegalArgumentException("Неизвестный тип поля: " + this.type);
        }
    }

    public Object getValue() {
        switch (this.type) {
            case STRING:
            case SOURCE:
                return this.stringValue;
            case NUMBER:
            case DATE:
                return this.numberValue;
            default:
                return null;
        }
    }

}