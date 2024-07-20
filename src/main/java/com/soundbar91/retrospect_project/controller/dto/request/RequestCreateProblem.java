package com.soundbar91.retrospect_project.controller.dto.request;

import com.soundbar91.retrospect_project.entity.Problem;
import com.soundbar91.retrospect_project.entity.User;

import java.util.List;
import java.util.Map;

public record RequestCreateProblem(
        String username,
        String title,
        String algorithms,
        String explanation,
        String input_explanation,
        String output_explanation,
        int memory,
        Map<String, String> runtime,
        int level,
        List<Map<String, String>> example_inout,
        List<Map<String, String>> testcase
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
