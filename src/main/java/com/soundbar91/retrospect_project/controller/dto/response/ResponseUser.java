package com.soundbar91.retrospect_project.controller.dto.response;

import com.soundbar91.retrospect_project.entity.User;

public record ResponseUser(
        Long id,
        String email,
        int level,
        double exp,
        String rol
) {
    public ResponseUser from(User user) {
        return new ResponseUser(
                user.getId(),
                user.getEmail(),
                user.getLevel(),
                user.getExp(),
                user.getRole()
        );
    }
}
