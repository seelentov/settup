package ru.vladislavkomkov.settup.model.data;

import jakarta.persistence.*;
import ru.vladislavkomkov.settup.exception.data.DataParseException;

import java.math.BigDecimal; // Используем BigDecimal для чисел, если нужна высокая точность
import java.time.Instant;
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

    private int intValue;
    private long longValue;

    private float floatValue;
    private double doubleValue;

    private String stringValue;

    private Instant instantValue;

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

    public void setValue(Object value) {
        switch (this.type) {
            case INT -> this.intValue = (int) value;
            case LONG -> this.longValue = (long) value;
            case FLOAT -> this.floatValue = (float) value;
            case DOUBLE -> this.doubleValue = (double) value;
            case STRING, SOURCE -> this.stringValue = (String) value;
            case DATE -> this.instantValue = (Instant) value;
            default -> throw new DataParseException("Unexpected field type");
        }
    }

    public Object getValue() {
        return switch (this.type) {
            case INT -> this.intValue;
            case LONG -> this.longValue;
            case FLOAT -> this.floatValue;
            case DOUBLE -> this.doubleValue;
            case STRING, SOURCE -> this.stringValue;
            case DATE -> this.instantValue;
            default -> throw new DataParseException("Unexpected field type");
        };
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public long getLongValue() {
        return longValue;
    }

    public void setLongValue(long longValue) {
        this.longValue = longValue;
    }

    public float getFloatValue() {
        return floatValue;
    }

    public void setFloatValue(float floatValue) {
        this.floatValue = floatValue;
    }

    public double getDoubleValue() {
        return doubleValue;
    }

    public void setDoubleValue(double doubleValue) {
        this.doubleValue = doubleValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public Instant getInstantValue() {
        return instantValue;
    }

    public void setInstantValue(Instant instantValue) {
        this.instantValue = instantValue;
    }
}