package com.soundbar91.retrospect_project.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ResultErrorCode implements ErrorCode {
    NOT_FOUND_RESULT(HttpStatus.NOT_FOUND, "존재하지 않는 결과입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
