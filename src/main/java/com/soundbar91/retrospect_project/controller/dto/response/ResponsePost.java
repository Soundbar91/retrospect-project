package com.soundbar91.retrospect_project.controller.dto.response;

import com.soundbar91.retrospect_project.entity.Post;

import java.time.LocalDateTime;

public record ResponsePost(
        Long id,
        String username,
        String title,
        String context,
        String category,
        int likes,
        LocalDateTime create_at,
        LocalDateTime modify_at
) {
    public static ResponsePost from(Post post) {
        return new ResponsePost(
                post.getId(),
                post.getUser().getUsername(),
                post.getTitle(),
                post.getContent(),
                post.getCategory().getCategoryName(),
                post.getLikes(),
                post.getCreate_at(),
                post.getModify_at()
        );
    }
}
