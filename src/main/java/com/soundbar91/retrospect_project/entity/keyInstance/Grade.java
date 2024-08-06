package com.soundbar91.retrospect_project.entity.keyInstance;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Grade {
    CORRECT("정답"),
    INCORRECT("오답"),
    COMPILER_ERROR("컴파일 에러"),
    RUNTIME_ERROR("런타임 에러"),
    TIME_ACCESS("시간 초과"),
    MEMORY_ACCESS("메모리 초과")
    ;

    private final String name;
}
