package ru.vladislavkomkov.settup.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
public class Role {
    public static final String ADMIN = "ADMIN";
    
    private int id;
    
    private String name;
}
