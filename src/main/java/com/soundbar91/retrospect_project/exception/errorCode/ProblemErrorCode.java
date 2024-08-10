package com.soundbar91.retrospect_project.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProblemErrorCode implements ErrorCode {

    NOT_FOUND_PROBLEM(HttpStatus.NOT_FOUND, "존재하지 않는 문제입니다."),
    NOT_FOUNT_ALGORITHM_CATEGORY(HttpStatus.NOT_FOUND, "존재하지 않는 알고리즘 분류입니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
