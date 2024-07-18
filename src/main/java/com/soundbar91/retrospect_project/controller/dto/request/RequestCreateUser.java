package com.soundbar91.retrospect_project.controller.dto.request;

import com.soundbar91.retrospect_project.entity.User;

public record RequestCreateUser(
        String email,
        String password
) {
    public User toEntity(String password) {
        return User.builder()
                .email(this.email)
                .password(password)
                .build();
    }
}