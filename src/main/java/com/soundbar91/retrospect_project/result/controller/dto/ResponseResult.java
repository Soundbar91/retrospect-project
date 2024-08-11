package com.soundbar91.retrospect_project.result.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.soundbar91.retrospect_project.result.entity.Result;

import java.time.LocalDateTime;

public record ResponseResult(
        Long id,
        Long problemId,
        String username,
        String grade,
        int memory,
        int runtime,
        String language,
        int codeLength,
        String code,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime submitAt
) {
    public static ResponseResult from(Result result) {
        return new ResponseResult(
                result.getId(),
                result.getProblem().getId(),
                result.getUser().getUsername(),
                result.getGrade().getName(),
                result.getMemory(),
                result.getRuntime(),
                result.getLanguage().getName(),
                result.getCodeLength(),
                result.getCode(),
                result.getSubmitAt()
        );
    }
}
