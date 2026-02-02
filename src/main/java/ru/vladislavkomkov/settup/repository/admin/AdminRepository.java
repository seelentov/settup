package ru.vladislavkomkov.settup.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vladislavkomkov.settup.model.admin.Admin;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Optional<Admin> findByUsername(String username);
}