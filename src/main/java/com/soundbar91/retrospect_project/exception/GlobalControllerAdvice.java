package com.soundbar91.retrospect_project.exception;

import com.soundbar91.retrospect_project.exception.errorCode.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ExceptionResponse> ApplicationException(ApplicationException e) {
        ErrorCode errorCode = e.getErrorCode();
        ExceptionResponse exceptionResponse = new ExceptionResponse(errorCode.name(), errorCode.getMessage());
        return new ResponseEntity<>(exceptionResponse, errorCode.getHttpStatus());
    }
    
}
