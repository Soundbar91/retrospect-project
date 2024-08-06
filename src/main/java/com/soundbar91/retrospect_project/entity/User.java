package com.soundbar91.retrospect_project.entity;

import com.soundbar91.retrospect_project.entity.keyInstance.Role;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

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
    @ColumnDefault("0.0")
    private double exp;

    @Column(insertable = false)
    @ColumnDefault("0")
    private Role role;

    @Column(insertable = false)
    @ColumnDefault("true")
    private boolean isActive;

    @Builder
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void solveProblem(int problemLevel) {
        exp += (problemLevel * 0.8);
        if (exp > this.role.getEND()) exp = this.role.getEND();
        if (exp > this.role.getExp() + this.role.getGAP()) role = Role.values()[this.role.ordinal() + 1];
    }

    public void deactivate() {
        isActive = false;
    }

}
