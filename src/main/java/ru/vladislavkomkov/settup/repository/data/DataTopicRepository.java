package ru.vladislavkomkov.settup.repository.data;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.vladislavkomkov.settup.model.data.DataTopic;

import java.util.Optional;

public interface DataTopicRepository extends JpaRepository<DataTopic, Integer> {
    Optional<DataTopic> findByName(String name);
}