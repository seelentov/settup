package ru.vladislavkomkov.settup.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.vladislavkomkov.settup.model.data.DataEntity;
import ru.vladislavkomkov.settup.model.data.DataField;
import ru.vladislavkomkov.settup.model.query.QueryRequest;
import jakarta.persistence.criteria.*;

import java.util.ArrayList;
import java.util.List;

public class DataEntitySpecifications {

    public static Specification<DataEntity> hasTopicId(Integer topicId) {
        return (root, query, cb) -> topicId == null ? null : cb.equal(root.get("topic").get("id"), topicId);
    }

    public static Specification<DataEntity> applyFilters(List<QueryRequest.Filter> filters) {
        return (root, query, cb) -> {
            if (filters == null || filters.isEmpty()) {
                return null;
            }

            List<Predicate> predicates = new ArrayList<>();

            for (QueryRequest.Filter filter : filters) {
                predicates.add(createPredicate(root, cb, filter));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static Predicate createPredicate(Root<DataEntity> root, CriteriaBuilder cb, QueryRequest.Filter filter) {
        Join<DataEntity, DataField> fieldJoin = root.join("dataFields", JoinType.INNER);

        Predicate fieldNamePredicate = cb.equal(fieldJoin.get("name"), filter.getFieldName());

        Predicate valuePredicate = createValuePredicate(fieldJoin, cb, filter);

        return cb.and(fieldNamePredicate, valuePredicate);
    }

    private static Predicate createValuePredicate(Join<DataEntity, DataField> fieldJoin, CriteriaBuilder cb, QueryRequest.Filter filter) {
        switch (filter.getOperator()) {
            case EQUALS:
                return cb.equal(fieldJoin.get("dataValue"), filter.getValue());
            case CONTAINS:
                return cb.like(fieldJoin.get("dataValue"), "%" + filter.getValue() + "%");
            case MORE:
                return createNumericPredicate(fieldJoin, cb, filter, ">");
            case LESS:
                return createNumericPredicate(fieldJoin, cb, filter, "<");
            default:
                return cb.conjunction();
        }
    }

    private static Predicate createNumericPredicate(Join<DataEntity, DataField> fieldJoin, CriteriaBuilder cb, QueryRequest.Filter filter, String operator) {
        try {
            Double value = Double.parseDouble(filter.getValue());

            Expression<Double> numericValue = cb.function("CAST", Double.class, fieldJoin.get("dataValue"));

            if (">".equals(operator)) {
                return cb.greaterThan(numericValue, value);
            } else {
                return cb.lessThan(numericValue, value);
            }
        } catch (NumberFormatException e) {
            return cb.disjunction();
        }
    }
}