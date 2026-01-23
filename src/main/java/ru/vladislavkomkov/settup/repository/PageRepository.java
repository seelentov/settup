package ru.vladislavkomkov.settup.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.vladislavkomkov.settup.model.Page;

public interface PageRepository extends JpaRepository<Page, Integer> {
}