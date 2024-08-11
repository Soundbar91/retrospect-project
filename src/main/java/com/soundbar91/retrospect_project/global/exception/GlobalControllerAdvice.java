package com.soundbar91.retrospect_project.global.exception;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.soundbar91.retrospect_project.global.exception.errorCode.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> httpMessageNotReadableException(HttpMessageNotReadableException e) {
        if (e.getCause() instanceof MismatchedInputException mismatchedInputException) {
            final String errorMessage = mismatchedInputException.getPath().get(0).getFieldName() + " 필드의 값이 잘못되었습니다.";
            ExceptionResponse exceptionResponse = new ExceptionResponse("MISSING_ARGUMENT", errorMessage);
            return new ResponseEntity<>(exceptionResponse, BAD_REQUEST);
        }
        else {
            final String errorMessage = "확인할 수 없는 형태의 데이터가 들어왔습니다";
            ExceptionResponse exceptionResponse = new ExceptionResponse("INVALID_ARGUMENT", errorMessage);
            return new ResponseEntity<>(exceptionResponse, BAD_REQUEST);
        }
    }

    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<ExceptionResponse> connectException(ConnectException e) {
        final String errorMessage = "채점 서버와 연결이 끊어졌습니다.";
        ExceptionResponse exceptionResponse = new ExceptionResponse("CONNECT_ERROR", errorMessage);
        return new ResponseEntity<>(exceptionResponse, SERVICE_UNAVAILABLE);
    }

}
