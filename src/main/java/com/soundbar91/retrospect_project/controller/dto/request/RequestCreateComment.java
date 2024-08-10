package com.soundbar91.retrospect_project.controller.dto.request;

import com.soundbar91.retrospect_project.entity.Comment;
import com.soundbar91.retrospect_project.entity.Post;
import com.soundbar91.retrospect_project.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

public record RequestCreateComment(
        @NotBlank(message = "댓글 내용은 필수 입력사항 입니다.")
        @Schema(description = "댓글 내용", defaultValue = "잘 보고 갑니다 !", requiredMode = REQUIRED)
        String context
) {
    public Comment toEntity(User user, Post post) {
        return Comment.builder()
                .context(this.context)
                .user(user)
                .post(post)
                .build();
    }
}
