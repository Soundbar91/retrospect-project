package com.soundbar91.retrospect_project.controller.dto.request;

import com.soundbar91.retrospect_project.entity.keyInstance.Category;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.hibernate.validator.constraints.Length;

public record RequestUpdatePost(
        @Length(message = "게시글 제목은 최대 100글자 입니다.", max = 100)
        String title,

        String context,

        Category category,

        @Min(0) @Max(9999)
        Integer likes
) {
}
