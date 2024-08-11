package com.soundbar91.retrospect_project.problem.controller.dto;

import com.soundbar91.retrospect_project.problem.entity.Problem;

import java.util.List;
import java.util.Map;

public record ResponseProblem(
        Long id,
        String username,
        String title,
        String algorithms,
        String problemExplanation,
        String inputExplanation,
        String outputExplanation,
        int memory,
        Map<String, Integer> runtime,
        int level,
        int submit,
        int answer,
        List<Map<String, Object>> exampleInOut
) {
    public static ResponseProblem from(Problem problem) {
        return new ResponseProblem(
                problem.getId(),
                problem.getUser().getUsername(),
                problem.getTitle(),
                problem.getAlgorithms(),
                problem.getProblemExplanation(),
                problem.getInputExplanation(),
                problem.getOutputExplanation(),
                problem.getMemory(),
                problem.getRuntime(),
                problem.getLevel(),
                problem.getSubmit(),
                problem.getAnswer(),
                problem.getExampleInOut()
        );
    }
}
