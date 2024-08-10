package com.soundbar91.retrospect_project.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

    NOT_FOUND_USER(NOT_FOUND, "존재하지 않는 회원입니다."),
    NOT_PERMISSION(UNAUTHORIZED, "접근 권한이 없습니다."),
    NOT_LOGIN(UNAUTHORIZED, "로그인이 필요합니다."),
    WITHDREW_USER(NOT_FOUND, "탈퇴한 유저입니다"),
    INVALID_USERNAME_LEN(BAD_REQUEST, "아이디는 최소 6자리, 최대 20자리입니다."),
    INVALID_USERNAME_PATTERN(BAD_REQUEST, "아이디는 영어와 숫자로만 구성되야합니다."),
    INVALID_EMAIL_PATTERN(BAD_REQUEST, "이메일은 user@example.com와 같은 형식으로 입력해야합니다."),
    INVALID_PASSWORD_LEN(BAD_REQUEST, "비밀번호는 최소 8자리, 최대 20자리입니다."),
    INVALID_PASSWORD_PATTERN(BAD_REQUEST, "비밀번호는 영어, 숫자 그리고 특수문자로 구성되며, !@#$%^&*()/ 중 하나 이상을 포함해야 합니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
