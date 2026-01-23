package ru.vladislavkomkov.settup.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.vladislavkomkov.settup.model.DataEntity;

public interface DataFieldRepository extends JpaRepository<DataEntity, Integer> {
}