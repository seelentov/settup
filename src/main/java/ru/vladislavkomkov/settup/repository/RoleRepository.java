package ru.vladislavkomkov.settup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vladislavkomkov.settup.model.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);
}