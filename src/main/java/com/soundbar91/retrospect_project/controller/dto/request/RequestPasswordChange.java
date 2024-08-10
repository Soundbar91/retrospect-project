package com.soundbar91.retrospect_project.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

public record RequestPasswordChange(
        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        @Schema(description = "현재 비밀번호", defaultValue = "1q2w3e4r1!", requiredMode = REQUIRED)
        String curPassword,

        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        @Schema(description = "새로운 비밀번호", defaultValue = "1q2w3e4r4$", requiredMode = REQUIRED)
        String newPassword
) {
}
