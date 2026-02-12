package ru.vladislavkomkov.settup.model.query;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class QueryRequest {

    private static final Pattern TEMPLATE_PATTERN = Pattern.compile("^\\{(.+)}$");

    private Integer topicId;
    private String topicName;
    private List<String> fields = new ArrayList<>();
    private List<Filter> filters = new ArrayList<>();
    private List<Sort> sorts = new ArrayList<>();
    private Integer page;
    private Integer size;

    public QueryRequest() {
    }

    private QueryRequest(QueryRequest other) {
        this.topicId = other.topicId;
        this.topicName = other.topicName;
        this.fields = new ArrayList<>(other.fields);
        this.filters = other.filters.stream().map(Filter::new).collect(Collectors.toList());
        this.sorts = other.sorts.stream().map(Sort::new).collect(Collectors.toList());
        this.page = other.page;
        this.size = other.size;
    }

    public boolean isSingle() {
        return page != null && size != null && page == 0 && size == 1;
    }

    public List<String> getFields() {
        return new ArrayList<>(fields); // защита от мутации
    }

    public void setFields(List<String> fields) {
        this.fields = fields != null ? new ArrayList<>(fields) : new ArrayList<>();
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    public List<Filter> getFilters() {
        return filters.stream().map(Filter::new).collect(Collectors.toList());
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters != null
                ? filters.stream().map(Filter::new).collect(Collectors.toList())
                : new ArrayList<>();
    }

    public List<Sort> getSorts() {
        return sorts.stream().map(Sort::new).collect(Collectors.toList());
    }

    public void setSorts(List<Sort> sorts) {
        this.sorts = sorts != null
                ? sorts.stream().map(Sort::new).collect(Collectors.toList())
                : new ArrayList<>();
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

    public QueryRequest resolveVariables(Map<String, String> pathVariables) {
        if (pathVariables == null || pathVariables.isEmpty()) {
            return new QueryRequest(this);
        }

        QueryRequest resolved = new QueryRequest();
        resolved.topicId = this.topicId;
        resolved.topicName = resolveTemplate(this.topicName, pathVariables);
        resolved.fields = this.fields.stream()
                .map(field -> resolveTemplate(field, pathVariables))
                .collect(Collectors.toList());
        resolved.filters = this.filters.stream()
                .map(filter -> filter.resolveVariables(pathVariables))
                .collect(Collectors.toList());
        resolved.sorts = this.sorts.stream()
                .map(sort -> sort.resolveVariables(pathVariables))
                .collect(Collectors.toList());
        resolved.page = this.page;
        resolved.size = this.size;

        return resolved;
    }

    private static String resolveTemplate(String value, Map<String, String> variables) {
        if (value == null) {
            return null;
        }
        Matcher matcher = TEMPLATE_PATTERN.matcher(value);
        if (matcher.matches()) {
            String varName = matcher.group(1);
            return variables.getOrDefault(varName, value);
        }
        return value;
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

        // Конструктор копирования
        public Filter(Filter other) {
            this.fieldName = other.fieldName;
            this.operator = other.operator;
            this.value = other.value;
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

        public Filter resolveVariables(Map<String, String> pathVariables) {
            Filter resolved = new Filter();
            resolved.fieldName = resolveTemplate(fieldName, pathVariables);
            resolved.operator = this.operator;
            resolved.value = resolveTemplate(value, pathVariables);
            return resolved;
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

        // Конструктор копирования
        public Sort(Sort other) {
            this.fieldName = other.fieldName;
            this.direction = other.direction;
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

        public Sort resolveVariables(Map<String, String> pathVariables) {
            Sort resolved = new Sort();
            resolved.fieldName = resolveTemplate(fieldName, pathVariables);
            resolved.direction = this.direction;
            return resolved;
        }
    }
}