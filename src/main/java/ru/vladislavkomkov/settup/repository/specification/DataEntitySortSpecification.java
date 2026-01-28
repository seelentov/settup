package ru.vladislavkomkov.settup.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.vladislavkomkov.settup.model.data.DataEntity;
import ru.vladislavkomkov.settup.model.data.DataField;
import ru.vladislavkomkov.settup.model.query.QueryRequest;
import jakarta.persistence.criteria.*;
import ru.vladislavkomkov.settup.model.query.SortDirection;

import java.util.ArrayList;
import java.util.List;

public class DataEntitySortSpecification {
    
    public static Specification<DataEntity> createSortSpecification(List<QueryRequest.Sort> sorts) {
        return (root, query, cb) -> {
            if (sorts == null || sorts.isEmpty()) {
                return null;
            }
            
            List<Order> orders = new ArrayList<>();
            
            for (QueryRequest.Sort sort : sorts) {
                String fieldName = sort.getFieldName();
                
                Subquery<String> subquery = query.subquery(String.class);
                Root<DataEntity> subRoot = subquery.correlate(root);
                Join<DataEntity, DataField> fieldJoin = subRoot.join("dataFields", JoinType.LEFT);
                
                Predicate conditions = cb.and(
                        cb.equal(fieldJoin.get("name"), fieldName),
                        cb.isTrue(fieldJoin.get("isActive"))
                );
                
                subquery.select(fieldJoin.get("dataValue"))
                        .where(conditions);
                
                Expression<String> sortExpression = subquery;
                
                Order order = sort.getDirection() == SortDirection.DESC ?
                        cb.desc(sortExpression) : cb.asc(sortExpression);
                
                orders.add(order);
            }
            
            orders.add(cb.asc(root.get("id")));
            
            query.orderBy(orders);
            
            return null;
        };
    }
    
    public static Specification<DataEntity> createSortSpecificationWithJoin(List<QueryRequest.Sort> sorts) {
        return (root, query, cb) -> {
            if (sorts == null || sorts.isEmpty()) {
                return null;
            }
            
            List<Order> orders = new ArrayList<>();
            
            for (QueryRequest.Sort sort : sorts) {
                String fieldName = sort.getFieldName();
                
                Join<DataEntity, DataField> sortJoin = root.join("dataFields", JoinType.LEFT);
                
                Predicate joinCondition = cb.and(
                        cb.equal(sortJoin.get("name"), fieldName),
                        cb.isTrue(sortJoin.get("isActive"))
                );
                sortJoin.on(joinCondition);
                
                Expression<?> sortExpression = sortJoin.get("dataValue");
                
                Order order = sort.getDirection() == SortDirection.DESC ?
                        cb.desc(sortExpression) : cb.asc(sortExpression);
                
                orders.add(order);
            }
            
            orders.add(cb.asc(root.get("id")));
            
            query.orderBy(orders);
            
            return null;
        };
    }
    
    public static Specification<DataEntity> createSingleFieldSort(String fieldName,
                                                                  SortDirection direction) {
        return (root, query, cb) -> {
            Subquery<String> subquery = query.subquery(String.class);
            Root<DataEntity> subRoot = subquery.correlate(root);
            Join<DataEntity, DataField> fieldJoin = subRoot.join("dataFields", JoinType.LEFT);
            
            Predicate conditions = cb.and(
                    cb.equal(fieldJoin.get("name"), fieldName),
                    cb.isTrue(fieldJoin.get("isActive"))
            );
            
            subquery.select(fieldJoin.get("dataValue"))
                    .where(conditions);
            
            Order order = direction == SortDirection.DESC ?
                    cb.desc(subquery) : cb.asc(subquery);
            
            query.orderBy(order, cb.asc(root.get("id")));
            
            return null;
        };
    }
}