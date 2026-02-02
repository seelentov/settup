package ru.vladislavkomkov.settup.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ru.vladislavkomkov.settup.model.data.DataField;

public interface DataFieldRepository extends JpaRepository<DataField, Integer> {
    List<DataField> findByEntityIdAndNameIn(Integer entityId, Collection<String> names);

    // Новый метод для оптимизации загрузки полей
    @Query("SELECT df FROM DataField df WHERE df.entity.id IN :entityIds AND df.name IN :fieldNames AND df.isActive = true")
    List<DataField> findByEntityIdInAndNameInAndIsActiveTrue(@Param("entityIds") Collection<Integer> entityIds, @Param("fieldNames") Collection<String> fieldNames);
}