package com.soundbar91.retrospect_project.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PostErrorCode implements ErrorCode {
    NOT_FOUND_POST(HttpStatus.NOT_FOUND, "존재하지 않는 게시물입니다."),
    DUPLICATE_LIKES(HttpStatus.BAD_REQUEST, "좋아요는 한 번만 누를 수 있습니다."),
    INVALID_TITLE_LEN(HttpStatus.BAD_REQUEST, "게시글 제목은 최대 100글자 입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
