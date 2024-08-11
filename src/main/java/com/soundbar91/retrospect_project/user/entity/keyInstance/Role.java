package com.soundbar91.retrospect_project.user.entity.keyInstance;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    BRONZE("브론즈", 0),
    SLIVER("실버", 50),
    GOLD("골드", 100),
    PLATINUM("플래티넘", 150),
    DIAMOND("다이아몬드", 200)
    ;

    private final String roleName;
    private final int exp;
    private final double END = 200.0;
    private final int GAP = 50;
}
