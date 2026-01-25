package ru.vladislavkomkov.settup.model.query;

import jakarta.persistence.*;
import ru.vladislavkomkov.settup.model.data.DataEntity;

@Entity
@Table(name = "query_rows")
public class QueryRow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private QueryType type;

    private String qValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "query_id")
    private Query query;

    public QueryRow() {
    }

    public QueryRow(QueryType type, String qValue) {
        this.type = type;
        this.qValue = qValue;
    }

    public QueryType getType() {
        return type;
    }

    public String getqValue() {
        return qValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}