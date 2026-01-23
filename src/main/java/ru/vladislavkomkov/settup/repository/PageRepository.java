package ru.vladislavkomkov.settup.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.vladislavkomkov.settup.model.Page;

public interface PageRepository extends JpaRepository<Page, Integer> {
    Optional<Page> findByUrl(String url);
}