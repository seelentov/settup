package ru.vladislavkomkov.settup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.vladislavkomkov.settup.model.data.DataEntity;

public interface DataEntityRepository extends JpaRepository<DataEntity, Integer>, JpaSpecificationExecutor<DataEntity> {
    List<DataEntity> findByTopicName(String topicName);

    Optional<DataEntity> findByIdAndTopicName(Integer id, String topicName);
}