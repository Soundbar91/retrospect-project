package com.soundbar91.retrospect_project.user.model;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Size(min = 6, max = 20)
    @Column(nullable = false, unique = true, length = 20)
    private String username;

    @Pattern(regexp = "^[a-zA-Z0-9]+@[0-9a-zA-Z]+\\.[a-z]+$")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @PositiveOrZero
    @Column(insertable = false, nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0.0")
    private double exp;

    @ColumnDefault("'USER'")
    @Enumerated(EnumType.STRING)
    @Column(insertable = false, nullable = false)
    private Role role;

    @ColumnDefault("'BRONZE'")
    @Enumerated(EnumType.STRING)
    @Column(insertable = false, nullable = false)
    private Tier tier;

    @ColumnDefault("true")
    @Column(insertable = false, nullable = false)
    private boolean isActive;

    @Builder
    public User(String username, String email, String password, boolean isActive, double exp, Tier tier) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.isActive = isActive;
        this.exp = exp;
        this.tier = tier;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void solveProblem(int problemLevel) {
        exp += (problemLevel * 0.8);
        if (exp > this.tier.getEND())
            exp = this.tier.getEND();
        if (exp > this.tier.getExp() + this.tier.getGAP())
            tier = Tier.values()[this.tier.ordinal() + 1];
    }

    public void deactivate() {
        isActive = false;
    }

}
