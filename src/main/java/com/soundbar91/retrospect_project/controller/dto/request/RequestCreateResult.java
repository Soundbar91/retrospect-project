package com.soundbar91.retrospect_project.controller.dto.request;

import com.soundbar91.retrospect_project.entity.Problem;
import com.soundbar91.retrospect_project.entity.Result;
import com.soundbar91.retrospect_project.entity.User;
import com.soundbar91.retrospect_project.entity.keyInstance.Grade;
import com.soundbar91.retrospect_project.entity.keyInstance.Language;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record RequestCreateResult(
        @NotNull(message = "언어는 필수 입력사항입니다.")
        Language language,

        @NotNull(message = "제출 코드는 필수 입력사항입니다.")
        String code
) {
    public Result toEntity(User user, Problem problem, Map<Object, Object> httpBody) {
        return Result.builder()
                .grade(Grade.valueOf((String) httpBody.get("status")))
                .problem(problem)
                .user(user)
                .language(this.language())
                .memory((int) httpBody.get("memory"))
                .errorMessage(httpBody.containsKey("error_message")? (String) httpBody.get("error_message") : "")
                .runtime((int) httpBody.get("runtime"))
                .code(this.code())
                .codeLength((int) httpBody.get("codeLen"))
                .build();
    }
}
