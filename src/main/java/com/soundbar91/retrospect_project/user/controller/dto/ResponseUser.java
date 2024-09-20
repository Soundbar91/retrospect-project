package com.soundbar91.retrospect_project.user.controller.dto;

import com.soundbar91.retrospect_project.user.model.User;

public record ResponseUser(
        String username,
        String email,
        String role,
        double exp
) {
    public static ResponseUser from(User user) {
        return new ResponseUser(
                user.getUsername(),
                user.getEmail(),
                user.getTier().getRoleName(),
                user.getExp()
        );
    }
}
