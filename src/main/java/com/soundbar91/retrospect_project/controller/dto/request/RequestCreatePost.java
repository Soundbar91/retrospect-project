package com.soundbar91.retrospect_project.controller.dto.request;

import com.soundbar91.retrospect_project.entity.Post;
import com.soundbar91.retrospect_project.entity.Problem;
import com.soundbar91.retrospect_project.entity.User;
import com.soundbar91.retrospect_project.entity.keyInstance.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record RequestCreatePost(
        @NotBlank(message = "게시글 제목은 필수 입력사항입니다.")
        @Length(message = "게시글 제목은 최대 100글자 입니다.", max = 100)
        String title,

        @NotBlank(message = "게시글 내용은 필수 입력사항입니다.")
        String context,

        @NotNull(message = "카테고리는 필수 입력사항입니다.")
        Category category
) {
    public Post toEntity(User user, Problem problem) {
        return Post.builder()
                .title(this.title)
                .content(this.context)
                .category(this.category)
                .problem(problem)
                .user(user)
                .build();
    }
}
