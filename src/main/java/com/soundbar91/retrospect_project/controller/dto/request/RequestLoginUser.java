package com.soundbar91.retrospect_project.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RequestLoginUser(
        @NotBlank(message = "이메일은 필수 입력 값입니다.")
        @Pattern(message = "이메일은 user@example.com와 같은 형식으로 입력해야합니다.",
                regexp = "^[a-zA-Z0-9]+@[0-9a-zA-Z]+\\.[a-z]+$")
        String email,

        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        @Size(message = "비밀번호는 최소 8자리, 최대 20자리입니다.", min = 8, max = 20)
        @Pattern(message = "비밀번호는 영어, 숫자 그리고 특수문자로 구성되며, !@#$%^&*()/ 중 하나 이상을 포함해야 합니다.",
                regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.+[!@#$%^&*()/]).{8,20}$")
        String password
) {

}
