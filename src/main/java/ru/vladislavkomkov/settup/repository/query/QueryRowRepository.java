package ru.vladislavkomkov.settup.repository.query;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.vladislavkomkov.settup.model.data.DataEntity;
import ru.vladislavkomkov.settup.model.query.QueryRow;

public interface QueryRowRepository extends JpaRepository<QueryRow, Integer>, JpaSpecificationExecutor<DataEntity> {
}