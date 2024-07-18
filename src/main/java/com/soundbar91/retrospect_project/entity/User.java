package com.soundbar91.retrospect_project.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@DynamicInsert
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(insertable = false)
    @ColumnDefault("1")
    private int level;

    @Column(insertable = false)
    @ColumnDefault("0.0")
    private double exp;

    @Column(length = 5, insertable = false)
    @ColumnDefault("'user'")
    private String role;

    @Builder
    public User(String email, String password, int level, double exp, String role) {
        this.email = email;
        this.password = password;
        this.level = level;
        this.exp = exp;
        this.role = role;
    }
}
