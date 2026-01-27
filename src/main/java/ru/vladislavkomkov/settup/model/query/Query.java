package ru.vladislavkomkov.settup.model.query;

import jakarta.persistence.*;
import ru.vladislavkomkov.settup.model.Page;
import ru.vladislavkomkov.settup.model.data.DataTopic;

import java.util.ArrayList;
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

    @OneToMany(mappedBy = "query", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<QuerySort> sorts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "page_id")
    private Page page;

    private int pageCount;

    private int pageSize;

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

    public QueryRequest toQueryRequest() {
        QueryRequest queryRequest = new QueryRequest();

        if (this.topic != null) {
            queryRequest.setTopicId(this.topic.getId());
            queryRequest.setTopicName(this.topic.getName());
        }

        if (this.query != null && !this.query.isEmpty()) {
            List<QueryRequest.Filter> filters = new ArrayList<>();

            for (QueryRow row : this.query) {
                QueryRequest.Filter filter = new QueryRequest.Filter(
                        row.getqKey(),
                        row.getType(),
                        row.getqValue()
                );
                filters.add(filter);
            }

            queryRequest.setFilters(filters);
        }

        queryRequest.setPage(this.pageCount);
        queryRequest.setSize(this.pageSize);

        if (this.sorts != null && !this.sorts.isEmpty()) {
            List<QueryRequest.Sort> sorts = new ArrayList<>();

            for (QuerySort row : this.sorts) {
                QueryRequest.Sort sort = new QueryRequest.Sort(
                        row.getqKey(),
                        row.getDirection()
                );
                sorts.add(sort);
            }

            queryRequest.setSorts(sorts);
        }

        return queryRequest;
    }

    public QueryRequest toQueryRequest(Integer page, Integer size) {
        QueryRequest queryRequest = this.toQueryRequest();
        if (page != null) queryRequest.setPage(page);
        if (size != null) queryRequest.setSize(size);
        return queryRequest;
    }

    public boolean isSingle() {
        return pageCount == 0 && pageSize == 1;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<QuerySort> getSorts() {
        return sorts;
    }

    public void setSorts(List<QuerySort> sorts) {
        this.sorts = sorts;
    }
}
