package com.soundbar91.retrospect_project.exception;

import com.soundbar91.retrospect_project.exception.errorCode.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ExceptionResponse> ApplicationException(ApplicationException e) {
        ErrorCode errorCode = e.getErrorCode();
        ExceptionResponse exceptionResponse = new ExceptionResponse(errorCode.name(), errorCode.getMessage());
        return new ResponseEntity<>(exceptionResponse, errorCode.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> dtoValidation(MethodArgumentNotValidException e) {
        List<String> list = new ArrayList<>();
        e.getBindingResult().getAllErrors().forEach((error)-> {
            String errorMessage = error.getDefaultMessage();
            list.add(errorMessage);
        });

        ExceptionResponse exceptionResponse = new ExceptionResponse("VERIFICATION_ERROR", list);
        return new ResponseEntity<>(exceptionResponse, BAD_REQUEST);
    }

}
