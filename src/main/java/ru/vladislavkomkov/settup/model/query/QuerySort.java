package ru.vladislavkomkov.settup.model.query;

import jakarta.persistence.*;

@Entity
@Table(name = "query_sorts")
public class QuerySort {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String qKey;
    private SortDirection direction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "query_id")
    private Query query;

    public QuerySort() {
    }

    public QuerySort(String qKey, SortDirection direction) {
        this.qKey = qKey;
        this.direction = direction;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getqKey() {
        return qKey;
    }

    public void setqKey(String qKey) {
        this.qKey = qKey;
    }

    public SortDirection getDirection() {
        return direction;
    }

    public void setDirection(SortDirection direction) {
        this.direction = direction;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }
}
