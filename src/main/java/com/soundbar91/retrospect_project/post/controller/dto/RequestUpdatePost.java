package com.soundbar91.retrospect_project.post.controller.dto;

import com.soundbar91.retrospect_project.post.entity.keyInstance.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

public record RequestUpdatePost(
        @NotBlank(message = "게시글 제목은 필수 입력사항입니다.")
        @Schema(description = "게시글 제목", defaultValue = "반례 공유합니다", requiredMode = REQUIRED)
        String title,

        @NotBlank(message = "게시글 내용은 필수 입력사항입니다.")
        @Schema(description = "게시글 내용", defaultValue = "123 넣어보세요", requiredMode = REQUIRED)
        String context,

        @NotNull(message = "카테고리는 필수 입력사항입니다.")
        @Schema(description = "게시글 카테고리", defaultValue = "COUNTEREXAMPLE", requiredMode = REQUIRED)
        Category category
) {
}
