package com.codeit.blog.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("예상치 못한 오류 발생 : {}", e.getMessage(), e);
        ErrorResponse errorResponse = new ErrorResponse(e, 500);
        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
    }
    
    // 커스텀 에러 처리
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(BaseException e) {
        log.error("커스텀 예외 발생 : code={}, message={}", e.getErrorCode(), e.getMessage());
        HttpStatus httpStatus = determineHttpStatus(e);
        ErrorResponse errorResponse = new ErrorResponse(e, httpStatus.value());
        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
    }

    private HttpStatus determineHttpStatus(BaseException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        return switch (errorCode) {
            case USER_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case DUPLICATE_USER -> HttpStatus.CONFLICT;
            case INVALID_USER_CREDENTIALS -> HttpStatus.UNAUTHORIZED;
            case INVALID_REQUEST, INVALID_USER_PARAMETER -> HttpStatus.BAD_REQUEST;
            case INTERNAL_SERVER_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }

    // 유효성 검사!!!
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("요청 유효성 검사 실패: {}", ex.getMessage());

        Map<String, Object> validationErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validationErrors.put(fieldName, errorMessage);
        });

        ErrorResponse response = new ErrorResponse(
                Instant.now(),
                "VALIDATION_ERROR",
                "요청 데이터 유효성 검사에 실패했습니다",
                validationErrors,
                ex.getClass().getSimpleName(),
                HttpStatus.BAD_REQUEST.value()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }


}
