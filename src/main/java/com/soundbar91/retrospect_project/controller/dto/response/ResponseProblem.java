package com.soundbar91.retrospect_project.controller.dto.response;

import com.soundbar91.retrospect_project.entity.Problem;

import java.util.List;
import java.util.Map;

public record ResponseProblem(
        Long id,
        String user_id,
        String title,
        String algorithms,
        String explanation,
        String input_explanation,
        String output_explanation,
        int memory,
        Map<String, String> runtime,
        int level,
        List<Map<String, String>> example_inout
) {
    public static ResponseProblem from(Problem problem) {
        return new ResponseProblem(
                problem.getId(),
                problem.getUser().getUsername(),
                problem.getTitle(),
                problem.getAlgorithms(),
                problem.getExplanation(),
                problem.getInput_explanation(),
                problem.getOutput_explanation(),
                problem.getMemory(),
                problem.getRuntime(),
                problem.getLevel(),
                problem.getExample_inout()
        );
    }
}
