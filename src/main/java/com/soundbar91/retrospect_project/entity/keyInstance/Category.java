package com.soundbar91.retrospect_project.entity.keyInstance;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    ERROR("오류"),
    QUESTION("질문"),
    COUNTEREXAMPLE("반례");

    private final String categoryName;
}
