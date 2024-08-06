package com.soundbar91.retrospect_project.controller.dto.response;

import com.soundbar91.retrospect_project.entity.Comment;

import java.time.LocalDateTime;

public record ResponseComment(
        String username,
        String context,
        int like,
        LocalDateTime create_at,
        LocalDateTime modify_at
) {
    public static ResponseComment from(Comment comment) {
        return new ResponseComment(
                comment.getUser().getUsername(),
                comment.getContext(),
                comment.getLikes().size(),
                comment.getCreate_at(),
                comment.getModify_at()
        );
    }
}
