package com.soundbar91.retrospect_project.user.controller.dto;

import com.soundbar91.retrospect_project.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

public record RequestCreateUser(
        @NotBlank(message = "아이디는 필수 입력 값입니다.")
        @Schema(description = "유저 아이디", defaultValue = "testUser", requiredMode = REQUIRED)
        String username,

        @NotBlank(message = "이메일은 필수 입력 값입니다.")
        @Schema(description = "유저 이메일", defaultValue = "testUser@naver.com", requiredMode = REQUIRED)
        String email,

        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        @Schema(description = "유저 비밀번호", defaultValue = "1q2w3e4r1!", requiredMode = REQUIRED)
        String password
) {
    public User toEntity(String password) {
        return User.builder()
                .username(this.username)
                .email(this.email)
                .password(password)
                .build();
    }
}
