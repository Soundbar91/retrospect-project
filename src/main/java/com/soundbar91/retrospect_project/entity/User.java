package com.soundbar91.retrospect_project.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String username;

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

    @OneToMany(mappedBy = "user")
    private List<Problem> problem = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Post> post = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> comment = new ArrayList<>();

    @Builder
    public User(String username, String email, String password, int level, double exp, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.level = level;
        this.exp = exp;
        this.role = role;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }
}
