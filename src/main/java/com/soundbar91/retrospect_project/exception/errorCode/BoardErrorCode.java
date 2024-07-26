package com.soundbar91.retrospect_project.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BoardErrorCode implements ErrorCode {
    NOT_FOUND_BOARD(HttpStatus.NOT_FOUND, "존재하지 않는 게시판입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
