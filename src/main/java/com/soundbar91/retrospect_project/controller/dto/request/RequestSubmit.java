package com.soundbar91.retrospect_project.controller.dto.request;

import com.soundbar91.retrospect_project.entity.Problem;
import com.soundbar91.retrospect_project.entity.Result;
import com.soundbar91.retrospect_project.entity.User;
import com.soundbar91.retrospect_project.entity.keyInstance.Grade;
import com.soundbar91.retrospect_project.entity.keyInstance.Language;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

public record RequestSubmit(
        @NotNull(message = "언어는 필수 입력사항입니다.")
        @Schema(description = "언어", defaultValue = "PYTHON", requiredMode = REQUIRED)
        Language language,

        @NotBlank(message = "제출 코드는 필수 입력사항입니다.")
        @Schema(description = "제출 코드", defaultValue = """
                a, b = map(int, input().split())
                print(a+b)
                """, requiredMode = REQUIRED)
        String code
) {
    public Result toEntity(User user, Problem problem, Map<String, Object> httpBody) {
        return Result.builder()
                .grade(Grade.valueOf((String) httpBody.get("result")))
                .problem(problem)
                .user(user)
                .language(this.language())
                .memory((int) httpBody.get("memory"))
                .runtime((int) httpBody.get("runtime"))
                .code(this.code())
                .codeLength((int) httpBody.get("codeLen"))
                .build();
    }
}
