package com.soundbar91.retrospect_project.exception;

import com.soundbar91.retrospect_project.exception.errorCode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApplicationException extends RuntimeException {
    private final ErrorCode errorCode;
}
