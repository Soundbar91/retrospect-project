package com.soundbar91.retrospect_project.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 20)
    private String password;

    @Column
    @ColumnDefault("1")
    private int level;

    @Column
    @ColumnDefault("0.0")
    private double exp;

    @Column(length = 5)
    @ColumnDefault("'user'")
    private String role;
}
