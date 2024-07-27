package com.soundbar91.retrospect_project.entity.keyInstance;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    BRONZE("브론즈"),
    SLIVER("실버"),
    GOLD("골드"),
    PLATINUM("플래티넘"),
    DIAMOND("다이아몬드")
    ;

    private final String roleName;
}
