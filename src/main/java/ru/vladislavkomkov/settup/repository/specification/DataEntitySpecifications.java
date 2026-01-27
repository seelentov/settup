package ru.vladislavkomkov.settup.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import ru.vladislavkomkov.settup.model.data.DataEntity;
import ru.vladislavkomkov.settup.model.data.DataField;
import ru.vladislavkomkov.settup.model.data.DataFieldType;
import ru.vladislavkomkov.settup.model.query.QueryRequest;
import jakarta.persistence.criteria.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DataEntitySpecifications {
    
    public static Specification<DataEntity> isActive(boolean active) {
        return (root, query, cb) -> cb.equal(root.get("isActive"), active);
    }
    
    public static Specification<DataEntity> hasTopicId(Integer topicId) {
        return (root, query, cb) -> topicId == null ? null : cb.equal(root.get("topic").get("id"), topicId);
    }
    
    public static Specification<DataEntity> applyFilters(List<QueryRequest.Filter> filters,
                                                         Map<String, DataFieldType> fieldTypes) {
        return (root, query, cb) -> {
            if (filters == null || filters.isEmpty()) {
                return null;
            }
            
            List<Predicate> predicates = new ArrayList<>();
            
            for (QueryRequest.Filter filter : filters) {
                DataFieldType fieldType = fieldTypes.get(filter.getFieldName());
                if (fieldType == null) {
                    continue;
                }
                predicates.add(createPredicate(root, cb, filter, fieldType));
            }
            
            return predicates.isEmpty() ? null : cb.and(predicates.toArray(new Predicate[0]));
        };
    }
    
    private static Predicate createPredicate(Root<DataEntity> root,
                                             CriteriaBuilder cb,
                                             QueryRequest.Filter filter,
                                             DataFieldType fieldType) {
        
        Join<DataEntity, DataField> fieldJoin = root.join("dataFields", JoinType.INNER);
        
        Predicate fieldNamePredicate = cb.equal(fieldJoin.get("name"), filter.getFieldName());
        Predicate valuePredicate = createValuePredicate(fieldJoin, cb, filter, fieldType);
        
        return cb.and(fieldNamePredicate, valuePredicate);
    }
    
    private static Predicate createValuePredicate(Join<DataEntity, DataField> fieldJoin,
                                                  CriteriaBuilder cb,
                                                  QueryRequest.Filter filter,
                                                  DataFieldType fieldType) {
        switch (filter.getOperator()) {
            case EQUALS:
                return cb.equal(fieldJoin.get("dataValue"), filter.getValue());
            case CONTAINS:
                return cb.like(fieldJoin.get("dataValue"), "%" + filter.getValue() + "%");
            case MORE:
                return createGreaterThanPredicate(fieldJoin, cb, filter, fieldType);
            case LESS:
                return createLessThanPredicate(fieldJoin, cb, filter, fieldType);
            default:
                return cb.conjunction();
        }
    }
    
    private static Predicate createGreaterThanPredicate(Join<DataEntity, DataField> fieldJoin,
                                                        CriteriaBuilder cb,
                                                        QueryRequest.Filter filter,
                                                        DataFieldType fieldType) {
        switch (fieldType) {
            case NUMBER:
                try {
                    Double value = Double.parseDouble(filter.getValue());
                    Expression<Double> numericValue = cb.function("CAST", Double.class, fieldJoin.get("dataValue"));
                    return cb.greaterThan(numericValue, value);
                } catch (NumberFormatException e) {
                    return cb.disjunction();
                }
            case DATE:
                try {
                    return cb.greaterThan(fieldJoin.get("dataValue"), filter.getValue());
                } catch (Exception e) {
                    return cb.disjunction();
                }
            default:
                return cb.disjunction();
        }
    }
    
    private static Predicate createLessThanPredicate(Join<DataEntity, DataField> fieldJoin,
                                                     CriteriaBuilder cb,
                                                     QueryRequest.Filter filter,
                                                     DataFieldType fieldType) {
        switch (fieldType) {
            case NUMBER:
                try {
                    Double value = Double.parseDouble(filter.getValue());
                    Expression<Double> numericValue = cb.function("CAST", Double.class, fieldJoin.get("dataValue"));
                    return cb.lessThan(numericValue, value);
                } catch (NumberFormatException e) {
                    return cb.disjunction();
                }
            case DATE:
                try {
                    return cb.greaterThan(fieldJoin.get("dataValue"), filter.getValue());
                } catch (Exception e) {
                    return cb.disjunction();
                }
            default:
                return cb.disjunction();
        }
    }
    
    private static Date parseDate(String dateString) throws Exception {
        java.text.SimpleDateFormat[] formats = {
                new java.text.SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", java.util.Locale.ENGLISH)
        };
        
        for (java.text.SimpleDateFormat format : formats) {
            try {
                return format.parse(dateString);
            } catch (java.text.ParseException e) {
            
            }
        }
        
        throw new Exception("Cannot parse date: " + dateString);
    }
}