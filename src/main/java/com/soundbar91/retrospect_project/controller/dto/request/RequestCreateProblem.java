package com.soundbar91.retrospect_project.controller.dto.request;

import com.soundbar91.retrospect_project.entity.Problem;
import com.soundbar91.retrospect_project.entity.User;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

import java.util.List;
import java.util.Map;

public record RequestCreateProblem(
        @NotBlank(message = "문제 제목은 필수 입력사항입니다.")
        @Length(message = "문제 제목은 최대 100글자 입니다.", max = 100)
        String title,

        @NotBlank(message = "알고리즘 분류는 필수 입력사항입니다.")
        String algorithms,

        @NotBlank(message = "문제 설명은 필수 입력사항입니다.")
        String explanation,

        @NotBlank(message = "문제 입력 설명은 필수 입력사항입니다.")
        String input_explanation,

        @NotBlank(message = "문제 출력 설명은 필수 입력사항입니다.")
        String output_explanation,

        @NotNull(message = "메모리 제한은 필수 입력사항입니다.")
        Integer memory,

        @NotEmpty(message = "제한 시간은 필수 입력사항입니다.")
        @Size(message = "C++, 자바, 파이선 모두 입력해야 합니다.", min = 3, max = 3)
        Map<String, Integer> runtime,

        @NotNull(message = "난이도는 필수 입력사항입니다.")
        @Min(message = "난이도는 최소 1입니다.", value = 1)
        @Max(message = "난이도는 최대 10입니다.", value = 10)
        Integer level,

        @NotEmpty(message = "예제 입출력은 필수 입력사항입니다.")
        List<@Size(message = "입력과 출력 모두 입력해야 합니다.", min = 2, max = 2)Map<String, String>> example_inout,

        @NotEmpty(message = "테스트 케이스는 필수 입력사항입니다.")
        List<@Size(message = "입력과 출력 모두 입력해야 합니다.", min = 2, max = 2)Map<String, String>> testcase
) {
    public Problem toEntity(User user) {
        return Problem.builder()
                .user(user)
                .title(this.title)
                .algorithms(this.algorithms)
                .explanation(this.explanation)
                .input_explanation(this.input_explanation)
                .output_explanation(this.output_explanation)
                .memory(this.memory)
                .runtime(this.runtime)
                .level(this.level)
                .example_inout(this.example_inout)
                .testcase(this.testcase)
                .build();
    }
}
