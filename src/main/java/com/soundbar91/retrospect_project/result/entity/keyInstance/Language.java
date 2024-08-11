package com.soundbar91.retrospect_project.result.entity.keyInstance;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Language {
    CPP("C++"),
    JAVA("자바"),
    PYTHON("파이썬")
    ;

    private final String name;
}
