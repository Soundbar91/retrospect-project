package com.soundbar91.retrospect_project.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.soundbar91.retrospect_project.entity.Result;

import java.time.LocalDateTime;

public record ResponseResult(
        Long id,
        String grade,
        int memory,
        int runtime,
        String language,
        int codeLength,
        String code,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime submit_at
) {
    public static ResponseResult from(Result result) {
        return new ResponseResult(
                result.getId(),
                result.getGrade().getName(),
                result.getMemory(),
                result.getRuntime(),
                result.getLanguage().getName(),
                result.getCodeLength(),
                result.getCode(),
                result.getSubmit_at()
        );
    }
}
