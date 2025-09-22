package com.codeit.blog.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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


}
