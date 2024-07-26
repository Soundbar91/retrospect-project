package com.soundbar91.retrospect_project.controller.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record RequestUpdateComment(
        String context,

        @Min(0) @Max(9999)
        Integer likes
) {
}
