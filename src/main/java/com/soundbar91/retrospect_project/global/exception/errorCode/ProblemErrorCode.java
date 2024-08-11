package com.soundbar91.retrospect_project.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProblemErrorCode implements ErrorCode {

    NOT_FOUND_PROBLEM(HttpStatus.NOT_FOUND, "존재하지 않는 문제입니다."),
    INVALID_ALGORITHM(HttpStatus.BAD_REQUEST, """
            알고리즘 분류는 기하, 구현, 그리디, 문자열, 자료구조, 그래프, 다이나믹 프로그래밍 중 하나 이상을 가져야 합니다. 
            """),
    INVALID_TITLE(HttpStatus.BAD_REQUEST, "제목의 길이는 최소 1, 최대 100 입니다."),
    INVALID_MEMORY(HttpStatus.BAD_REQUEST, "메모리 제한은 최소 1MB, 최대 2048MB 입니다"),
    INVALID_RUNTIME_KEY(HttpStatus.BAD_REQUEST, "제한 시간의 키 값은 cpp, java, python 언어를 입력해야 합니다"),
    INVALID_RUNTIME_VALUE(HttpStatus.BAD_REQUEST, "제한 시간은 최소 100ms, 최대 20000ms 입니다."),
    INVALID_LEVEL(HttpStatus.BAD_REQUEST, "난이도는 최소 1, 최대 10 입니다"),
    INVALID_EXAMPLE_KEY(HttpStatus.BAD_REQUEST, "예제의 키값은 input와 output로 구성되어야 합니다."),
    INVALID_TESTCASE_KEY(HttpStatus.BAD_REQUEST, "테스트 케이스의 키값은 input와 output로 구성되어야 합니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
