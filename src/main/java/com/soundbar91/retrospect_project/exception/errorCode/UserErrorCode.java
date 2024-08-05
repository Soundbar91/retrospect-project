package com.soundbar91.retrospect_project.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    NOT_PERMISSION(HttpStatus.UNAUTHORIZED, "접근 권한이 없습니다."),
    NOT_LOGIN(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
