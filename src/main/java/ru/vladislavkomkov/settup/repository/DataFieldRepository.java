package ru.vladislavkomkov.settup.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.vladislavkomkov.settup.model.data.DataField;

public interface DataFieldRepository extends JpaRepository<DataField, Integer> {
    List<DataField> findByEntityIdAndNameIn(Integer entityId, Collection<String> names);
}