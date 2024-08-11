package com.soundbar91.retrospect_project.comment.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.soundbar91.retrospect_project.comment.entity.Comment;

import java.time.LocalDateTime;

public record ResponseComment(
        String username,
        String context,
        int like,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime create_at,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime modify_at
) {
    public static ResponseComment from(Comment comment) {
        return new ResponseComment(
                comment.getUser().getUsername(),
                comment.getContext(),
                comment.getLikes().size(),
                comment.getCreateAt(),
                comment.getModifyAt()
        );
    }
}
