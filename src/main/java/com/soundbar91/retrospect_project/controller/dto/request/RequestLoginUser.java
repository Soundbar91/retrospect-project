package com.soundbar91.retrospect_project.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

public record RequestLoginUser(
        @NotBlank(message = "아이디는 필수 입력 값입니다.")
        @Size(message = "아이디는 최소 6자리, 최대 20자리입니다.", min = 6, max = 20)
        @Pattern(message = "아이디는 영어와 숫자로만 구성되야합니다. ", regexp = "^[a-zA-Z0-9]+$")
        @Schema(description = "유저 아이디", defaultValue = "testUser", requiredMode = REQUIRED)
        String username,

        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        @Size(message = "비밀번호는 최소 8자리, 최대 20자리입니다.", min = 8, max = 20)
        @Pattern(message = "비밀번호는 영어, 숫자 그리고 특수문자로 구성되며, !@#$%^&*()/ 중 하나 이상을 포함해야 합니다.",
                regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.+[!@#$%^&*()/]).{8,20}$")
        @Schema(description = "유저 비밀번호", defaultValue = "1q2w3e4r1!", requiredMode = REQUIRED)
        String password
) {

}
