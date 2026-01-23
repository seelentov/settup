package ru.vladislavkomkov.settup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.vladislavkomkov.settup.model.DataEntity;

public interface DataEntityRepository extends JpaRepository<DataEntity, Integer> {
    List<DataEntity> findByTopicName(String topicName);
    Optional<DataEntity> findByIdAndTopicName(Integer id, String topicName);
}