package ru.vladislavkomkov.settup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vladislavkomkov.settup.model.Admin;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Optional<Admin> findByUsername(String username);
}