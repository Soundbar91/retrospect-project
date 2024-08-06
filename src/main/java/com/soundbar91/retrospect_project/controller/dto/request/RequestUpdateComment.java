package com.soundbar91.retrospect_project.controller.dto.request;


import jakarta.validation.constraints.NotBlank;

public record RequestUpdateComment(
        @NotBlank(message = "댓글 내용은 필수 입력사항 입니다.")
        String context
) {
}
