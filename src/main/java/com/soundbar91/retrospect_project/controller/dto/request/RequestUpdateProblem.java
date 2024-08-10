package com.soundbar91.retrospect_project.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

import java.util.List;
import java.util.Map;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

public record RequestUpdateProblem(
        @NotBlank(message = "문제 제목은 필수 입력사항입니다.")
        @Length(message = "문제 제목은 최대 100글자 입니다.", max = 100)
        @Schema(description = "문제 제목", defaultValue = "A + B", requiredMode = REQUIRED)
        String title,

        @NotBlank(message = "알고리즘 분류는 필수 입력사항입니다.")
        @Schema(description = "알고리즘 분류", defaultValue = "구현", requiredMode = REQUIRED)
        String algorithms,

        @NotBlank(message = "문제 설명은 필수 입력사항입니다.")
        @Schema(description = "문제 설명", defaultValue = """
                두 정수 A와 B를 입력받은 다음, A+B를 출력하는 프로그램을 작성하시오.
                """, requiredMode = REQUIRED)
        String explanation,

        @NotBlank(message = "문제 입력 설명은 필수 입력사항입니다.")
        @Schema(description = "문제 입력 설명", defaultValue = """
                첫째 줄에 A와 B가 주어진다. (0 < A, B < 10)
                """, requiredMode = REQUIRED)
        String input_explanation,

        @NotBlank(message = "문제 출력 설명은 필수 입력사항입니다.")
        @Schema(description = "문제 출력 설명", defaultValue = """
                첫째 줄에 A+B를 출력한다.
                """, requiredMode = REQUIRED)
        String output_explanation,

        @NotNull(message = "메모리 제한은 필수 입력사항입니다.")
        @Schema(description = "메모리 제한", defaultValue = "128", requiredMode = REQUIRED)
        Integer memory,

        @NotEmpty(message = "제한 시간은 필수 입력사항입니다.")
        @Size(message = "C++, 자바, 파이선 모두 입력해야 합니다.", min = 3, max = 3)
        @Schema(description = "제한 시간", defaultValue = """
                {
                    "cpp": 2000,
                    "java": 2000,
                    "python": 2000
                  }
                """, requiredMode = REQUIRED)
        Map<String, Integer> runtime,

        @NotNull(message = "난이도는 필수 입력사항입니다.")
        @Min(message = "난이도는 최소 1입니다.", value = 1)
        @Max(message = "난이도는 최대 10입니다.", value = 10)
        @Schema(description = "난이도", defaultValue = "1", requiredMode = REQUIRED)
        Integer level,

        @NotEmpty(message = "예제 입출력은 필수 입력사항입니다.")
        @Schema(description = "예제 입출력", defaultValue = """
                [
                    {
                      "input": "1 2",
                      "output": "3"
                    }
                  ]
                """, requiredMode = REQUIRED)
        List<@Size(message = "입력과 출력 모두 입력해야 합니다.", min = 2, max = 2)Map<String, Object>> example_inout,

        @NotEmpty(message = "테스트 케이스는 필수 입력사항입니다.")
        @Schema(description = "테스트 케이스", defaultValue = """
                [
                    {
                      "input": "1 2",
                      "output": "3"
                    }
                  ]
                """, requiredMode = REQUIRED)
        List<@Size(message = "입력과 출력 모두 입력해야 합니다.", min = 2, max = 2)Map<String, Object>> testcase
) {
}
