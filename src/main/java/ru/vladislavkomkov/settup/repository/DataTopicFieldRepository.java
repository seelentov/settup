package ru.vladislavkomkov.settup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.vladislavkomkov.settup.model.data.DataTopicField;

import java.util.List;

@Repository
public interface DataTopicFieldRepository extends JpaRepository<DataTopicField, Integer> {
    List<DataTopicField> findByTopicIdAndIsActiveTrue(Integer topicId);

    DataTopicField findByNameAndTopicId(String name, Integer topicId);
}