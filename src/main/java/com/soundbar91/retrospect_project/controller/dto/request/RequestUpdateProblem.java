package com.soundbar91.retrospect_project.controller.dto.request;

import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

import java.util.List;
import java.util.Map;

public record RequestUpdateProblem(
        @Length(message = "문제 제목은 최대 100글자 입니다.", max = 100)
        String title,
        String algorithms,
        String explanation,
        String input_explanation,
        String output_explanation,
        Integer memory,
        @Size(message = "C++, 자바, 파이선 모두 입력해야 합니다.", min = 3, max = 3)
        Map<String, Integer> runtime,
        Integer level,
        Boolean isPost,
        List<@Size(message = "입력과 출력 모두 입력해야 합니다.", min = 2, max = 2)Map<String, String>> example_inout,
        List<@Size(message = "입력과 출력 모두 입력해야 합니다.", min = 2, max = 2)Map<String, String>> testcase
) {
}
