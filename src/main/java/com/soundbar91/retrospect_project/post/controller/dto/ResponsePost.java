package com.soundbar91.retrospect_project.post.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.soundbar91.retrospect_project.post.entity.Post;

import java.time.LocalDateTime;

public record ResponsePost(
        Long id,
        String username,
        String title,
        String context,
        String category,
        int likes,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createAt,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime modifyAt
) {
    public static ResponsePost from(Post post) {
        return new ResponsePost(
                post.getId(),
                post.getUser().getUsername(),
                post.getTitle(),
                post.getContent(),
                post.getCategory().getCategoryName(),
                post.getLikes().size(),
                post.getCreateAt(),
                post.getModifyAt()
        );
    }
}
