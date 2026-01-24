package ru.vladislavkomkov.settup.model;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {
    public static final String ADMIN = "ADMIN";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
