package com.soundbar91.retrospect_project.exception.errorCode;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String name();
    HttpStatus getHttpStatus();
    String getMessage();

}
