package com.soundbar91.retrospect_project.controller.dto.request;

import java.util.List;
import java.util.Map;

public record RequestUpdateProblem(
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
}
