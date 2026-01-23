package ru.vladislavkomkov.settup.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.vladislavkomkov.settup.model.DataTopic;

public interface DataTopicRepository extends JpaRepository<DataTopic, Integer> {
}