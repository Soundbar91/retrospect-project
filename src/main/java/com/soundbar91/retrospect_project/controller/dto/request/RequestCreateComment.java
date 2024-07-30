package com.soundbar91.retrospect_project.controller.dto.request;

import com.soundbar91.retrospect_project.entity.Comment;
import com.soundbar91.retrospect_project.entity.Post;
import com.soundbar91.retrospect_project.entity.User;
import jakarta.validation.constraints.NotEmpty;

public record RequestCreateComment(
        @NotEmpty(message = "댓글 내용은 필수 입력사항 입니다.")
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
