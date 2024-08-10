package com.soundbar91.retrospect_project.entity;

import com.soundbar91.retrospect_project.entity.keyInstance.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    @Size(min = 6, max = 20)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    @Pattern(regexp = "^[a-zA-Z0-9]+@[0-9a-zA-Z]+\\.[a-z]+$")
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(insertable = false, nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0.0")
    @PositiveOrZero
    private double exp;

    @Column(insertable = false, nullable = false)
    @ColumnDefault("0")
    private Role role;

    @Column(insertable = false, nullable = false)
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
