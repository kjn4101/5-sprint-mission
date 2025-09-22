package com.codeit.exception.exception;

import com.codeit.exception.dto.ErrorResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.nio.file.AccessDeniedException;

//@ControllerAdvice // 일반 에러 처리
@RestControllerAdvice // Rest 전용 글로벌 예외 처리
@Order(1)
public class GlobalRestExceptionHandler {

    // 1. 잘못된 요청
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        System.out.println("IllegalArgumentException: " + ex.getMessage()); // log

        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.of(ErrorCode.INVALID_REQUEST, ex.getMessage()));
    }

    // 2. 사용자 없음
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        System.out.println("UserNotFoundException: " + ex.getMessage()); // log

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of(ErrorCode.USER_NOT_FOUND, ex.getMessage()));
    }

    // 3. 접근 거부
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        System.out.println("AccessDeniedException: " + ex.getMessage()); // log

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.of(ErrorCode.ACCESS_DENIED, ex.getMessage()));
    }

    // 4. 그 외 예외 (Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllUnhandled(Exception ex, WebRequest request) {
        System.out.println("Exception: " + ex.getMessage());
        System.out.println("Request URI: " + request.getDescription(false));
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR, "예기치 못한 오류가 발생했습니다."));
    }
}
