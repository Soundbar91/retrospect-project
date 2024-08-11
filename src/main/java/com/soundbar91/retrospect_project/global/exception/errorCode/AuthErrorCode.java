package com.soundbar91.retrospect_project.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "이미 가입된 이메일입니다"),
    DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST, "이미 가입된 아이디입니다"),
    NOT_MATCH_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 틀렸습니다")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
