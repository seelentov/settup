package ru.vladislavkomkov.settup.model.query;

import jakarta.persistence.*;
import ru.vladislavkomkov.settup.model.Page;
import ru.vladislavkomkov.settup.model.data.DataTopic;

import java.util.List;

@Entity
@Table(name = "queries")
public class Query {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "data_topic_id")
    private DataTopic topic;

    @OneToMany(mappedBy = "query", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<QueryRow> query;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "page_id")
    private Page page;

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

    public DataTopic getTopic() {
        return topic;
    }

    public void setTopic(DataTopic topic) {
        this.topic = topic;
    }

    public List<QueryRow> getQuery() {
        return query;
    }

    public void setQuery(List<QueryRow> query) {
        this.query = query;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
