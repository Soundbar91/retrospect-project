package com.soundbar91.retrospect_project.controller.dto.request;

import jakarta.validation.constraints.NotNull;

public record RequestUpdateComment(
        @NotNull(message = "댓글 내용은 필수 입력사항입니다.")
        String context
) {
}
