package com.soundbar91.retrospect_project.controller.dto.response;

import com.soundbar91.retrospect_project.entity.User;

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
                user.getRole().getRoleName(),
                user.getExp()
        );
    }
}
