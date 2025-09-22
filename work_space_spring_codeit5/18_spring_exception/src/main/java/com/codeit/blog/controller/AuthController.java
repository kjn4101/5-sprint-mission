package com.codeit.blog.controller;


import com.codeit.blog.dto.base.ApiResult;
import com.codeit.blog.dto.user.UserLoginRequest;
import com.codeit.blog.dto.user.UserResponse;
import com.codeit.blog.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping(path = "login")
    public ResponseEntity<ApiResult<UserResponse>> login( UserLoginRequest loginRequest) {

        UserResponse user = authService.login(loginRequest);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResult.ok(user));
    }

    @PostMapping(path = "login2")
    public ResponseEntity<ApiResult<UserResponse>> login2(String username, String password) {
        UserLoginRequest request = new UserLoginRequest(username, password);
        UserResponse user = authService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResult.ok(user));
    }
}
