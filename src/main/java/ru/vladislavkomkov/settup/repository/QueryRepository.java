package ru.vladislavkomkov.settup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.vladislavkomkov.settup.model.data.DataEntity;
import ru.vladislavkomkov.settup.model.query.Query;

import java.util.List;
import java.util.Optional;

public interface QueryRepository extends JpaRepository<Query, Integer>, JpaSpecificationExecutor<DataEntity> {
}