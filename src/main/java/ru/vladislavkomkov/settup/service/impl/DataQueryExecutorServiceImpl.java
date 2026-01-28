package ru.vladislavkomkov.settup.service.impl;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import ru.vladislavkomkov.settup.model.data.DataFieldType;
import ru.vladislavkomkov.settup.model.data.DataTopicField;
import ru.vladislavkomkov.settup.model.query.QueryRequest;
import ru.vladislavkomkov.settup.model.query.SortDirection;
import ru.vladislavkomkov.settup.repository.DataTopicFieldRepository;
import ru.vladislavkomkov.settup.service.DataQueryExecutorService;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static ru.vladislavkomkov.settup.model.data.DataField.DATE_FORMAT;

@Service
public class DataQueryExecutorServiceImpl implements DataQueryExecutorService {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final DataTopicFieldRepository topicFieldRepository;

    public DataQueryExecutorServiceImpl(NamedParameterJdbcTemplate jdbcTemplate, DataTopicFieldRepository topicFieldRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.topicFieldRepository = topicFieldRepository;
    }

    @Override
    public List<Map<String, Object>> executeQuery(QueryRequest request) {
        List<DataTopicField> topicFields = topicFieldRepository.findByTopicIdAndIsActiveTrue(request.getTopicId());

        if (topicFields.isEmpty()) {
            return Collections.emptyList();
        }

        QueryBuilder builder = new QueryBuilder(request, topicFields);
        String sql = builder.build();
        MapSqlParameterSource params = builder.getParameters();

        return jdbcTemplate.queryForList(sql, params);
    }

    private static class QueryBuilder {
        private final QueryRequest request;
        private final List<DataTopicField> topicFields;
        private final MapSqlParameterSource parameters = new MapSqlParameterSource();
        private final Map<String, DataFieldType> fieldTypesByName;
        private final Map<String, String> fieldAliases = new HashMap<>();
        private final Set<String> requestedFieldNames;

        public QueryBuilder(QueryRequest request, List<DataTopicField> topicFields) {
            this.request = request;
            this.topicFields = topicFields;
            this.fieldTypesByName = topicFields.stream()
                    .collect(Collectors.toMap(DataTopicField::getName, DataTopicField::getType));

            if (request.getFields() == null || request.getFields().isEmpty()) {
                this.requestedFieldNames = fieldTypesByName.keySet();
            } else {
                this.requestedFieldNames = new HashSet<>(request.getFields());
            }
        }

        public String build() {
            StringBuilder sql = new StringBuilder();

            buildSelectClause(sql);

            buildFromAndJoins(sql);

            buildWhereClause(sql);

            buildOrderByClause(sql);

            buildPagination(sql);

            return sql.toString();
        }

        private void buildSelectClause(StringBuilder sql) {
            sql.append("SELECT\n");
            sql.append("de.id\n");

            for (String fieldName : requestedFieldNames) {
                DataTopicField field = topicFields.stream()
                        .filter(f -> f.getName().equals(fieldName))
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("Field not found: " + fieldName));

                String alias = getFieldAlias(field.getName());
                sql.append(",\n  ");

                if (field.getType() == DataFieldType.NUMBER) {
                    sql.append("CAST(").append(alias).append(".data_value AS INT)");
                } else if (field.getType() == DataFieldType.DATE) {
                    sql.append("PARSEDATETIME(").append(alias).append(".data_value, '" + DATE_FORMAT + "')");
                } else {
                    sql.append(alias).append(".data_value");
                }

                sql.append(" AS \"").append(field.getName()).append("\"");
            }

            sql.append("\n");
        }

        private void buildFromAndJoins(StringBuilder sql) {
            sql.append("FROM data_entities de\n");

            for (DataTopicField field : topicFields) {
                String alias = getFieldAlias(field.getName());
                sql.append("JOIN data_fields ").append(alias).append(" ON ").append(alias).append(".data_entity_id = de.id").append(" AND ").append(alias).append(".name = :").append(field.getName()).append("_name").append("\n");

                parameters.addValue(field.getName() + "_name", field.getName());
            }
        }

        private void buildWhereClause(StringBuilder sql) {
            List<String> whereConditions = new ArrayList<>();

            whereConditions.add("de.data_topic_id = :topicId");
            parameters.addValue("topicId", request.getTopicId());

            int filterIndex = 0;
            for (QueryRequest.Filter filter : request.getFilters()) {
                String fieldName = filter.getFieldName();
                DataFieldType fieldType = fieldTypesByName.get(fieldName);

                if (fieldType == null) {
                    continue;
                }

                String alias = getFieldAlias(fieldName);
                String paramName = "filter_" + fieldName + "_" + filterIndex;

                switch (filter.getOperator()) {
                    case EQUALS:
                        if (fieldType == DataFieldType.NUMBER) {
                            whereConditions.add("CAST(" + alias + ".data_value AS INT) = :" + paramName);
                            parameters.addValue(paramName, Integer.parseInt(filter.getValue()));
                        } else if (fieldType == DataFieldType.DATE) {
                            whereConditions.add("PARSEDATETIME(" + alias + ".data_value, '" + DATE_FORMAT + "') = :" + paramName);
                            parameters.addValue(paramName, parseDate(filter.getValue()));
                        } else {
                            whereConditions.add(alias + ".data_value = :" + paramName);
                            parameters.addValue(paramName, filter.getValue());
                        }
                        break;

                    case MORE:
                        if (fieldType == DataFieldType.NUMBER) {
                            whereConditions.add("CAST(" + alias + ".data_value AS INT) > :" + paramName);
                            parameters.addValue(paramName, Integer.parseInt(filter.getValue()));
                        } else if (fieldType == DataFieldType.DATE) {
                            whereConditions.add("PARSEDATETIME(" + alias + ".data_value, '" + DATE_FORMAT + "') > :" + paramName);
                            parameters.addValue(paramName, parseDate(filter.getValue()));
                        }
                        break;

                    case LESS:
                        if (fieldType == DataFieldType.NUMBER) {
                            whereConditions.add("CAST(" + alias + ".data_value AS INT) < :" + paramName);
                            parameters.addValue(paramName, Integer.parseInt(filter.getValue()));
                        } else if (fieldType == DataFieldType.DATE) {
                            whereConditions.add("PARSEDATETIME(" + alias + ".data_value, '" + DATE_FORMAT + "') < :" + paramName);
                            parameters.addValue(paramName, parseDate(filter.getValue()));
                        }
                        break;

                    case CONTAINS:
                        whereConditions.add(alias + ".data_value LIKE :" + paramName);
                        parameters.addValue(paramName, "%" + filter.getValue() + "%");
                        break;
                }

                filterIndex++;
            }

            if (!whereConditions.isEmpty()) {
                sql.append("WHERE ").append(String.join(" AND ", whereConditions)).append("\n");
            }
        }

        private void buildOrderByClause(StringBuilder sql) {
            if (request.getSorts() == null || request.getSorts().isEmpty()) {
                return;
            }

            List<String> orderParts = new ArrayList<>();
            for (QueryRequest.Sort sort : request.getSorts()) {
                String fieldName = sort.getFieldName();
                DataFieldType fieldType = fieldTypesByName.get(fieldName);

                if (fieldType == null) {
                    continue;
                }

                String alias = getFieldAlias(fieldName);
                String direction = (sort.getDirection() == SortDirection.DESC) ? "DESC" : "ASC";

                if (fieldType == DataFieldType.NUMBER) {
                    orderParts.add("CAST(" + alias + ".data_value AS INT) " + direction);
                } else if (fieldType == DataFieldType.DATE) {
                    orderParts.add("PARSEDATETIME(" + alias + ".data_value, 'EEE MMM dd HH:mm:ss zzz yyyy') " + direction);
                } else {
                    orderParts.add(alias + ".data_value " + direction);
                }
            }

            if (!orderParts.isEmpty()) {
                sql.append("ORDER BY ").append(String.join(", ", orderParts)).append("\n");
            }
        }

        private void buildPagination(StringBuilder sql) {
            if (request.getPage() != null && request.getSize() != null) {
                int offset = request.getPage() * request.getSize();
                sql.append("LIMIT :limit OFFSET :offset\n");
                parameters.addValue("limit", request.getSize());
                parameters.addValue("offset", offset);
            }
        }

        private String getFieldAlias(String fieldName) {
            return fieldAliases.computeIfAbsent(fieldName, name -> "df_" + name);
        }

        private java.util.Date parseDate(String dateStr) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                ZonedDateTime zdt = ZonedDateTime.parse(dateStr, formatter);
                return Date.from(zdt.toInstant());
            } catch (Exception e) {
                try {
                    return java.sql.Date.valueOf(dateStr);
                } catch (Exception ex) {
                    throw new IllegalArgumentException("Invalid date format: " + dateStr, e);
                }
            }
        }

        public MapSqlParameterSource getParameters() {
            return parameters;
        }
    }
}