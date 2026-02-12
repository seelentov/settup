package ru.vladislavkomkov.settup.model.query;

import ru.vladislavkomkov.settup.model.data.DataFieldType;

import java.util.ArrayList;
import java.util.List;

public class QueryRequest {
    private Integer topicId;
    private String topicName;
    private List<String> fields = new ArrayList<>();
    private List<Filter> filters = new ArrayList<>();
    private List<Sort> sorts = new ArrayList<>();
    private Integer page;
    private Integer size;

    public QueryRequest() {
    }

    public boolean isSingle() {
        return page == 0 && size == 1;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }
    
    public String getTopicName() {
        return topicName;
    }
    
    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }
    
    public static class Filter {
        private String fieldName;
        private QueryType operator;
        private String value;

        public Filter() {
        }

        public Filter(String fieldName, QueryType operator, String value) {
            this.fieldName = fieldName;
            this.operator = operator;
            this.value = value;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public QueryType getOperator() {
            return operator;
        }

        public void setOperator(QueryType operator) {
            this.operator = operator;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class Sort {
        private String fieldName;
        private SortDirection direction;

        public Sort() {
        }

        public Sort(String fieldName, SortDirection direction) {
            this.fieldName = fieldName;
            this.direction = direction;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public SortDirection getDirection() {
            return direction;
        }

        public void setDirection(SortDirection direction) {
            this.direction = direction;
        }
    }

    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    public List<Sort> getSorts() {
        return sorts;
    }

    public void setSorts(List<Sort> sorts) {
        this.sorts = sorts;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}