package com.soundbar91.retrospect_project.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CompilerException extends RuntimeException {
    private final String message;
}
