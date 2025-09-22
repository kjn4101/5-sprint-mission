package com.codeit.blog.controller;


import com.codeit.blog.dto.base.ApiResult;
import com.codeit.blog.dto.user.UserLoginRequest;
import com.codeit.blog.dto.user.UserResponse;
import com.codeit.blog.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
@Slf4j // log를 활성화하는 어노테이션, lombok
public class AuthController {

    //    @Slf4j로 생략되는 문장
//    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    @PostMapping(path = "login")
    public ResponseEntity<ApiResult<UserResponse>> login( UserLoginRequest loginRequest) {
        // 사용자 로그 테스트
        log.trace("@@ trace - loginRequest : {}", loginRequest);
        log.debug("@@ debug - loginRequest : {}", loginRequest);
        log.info("@@ info - loginRequest : {}", loginRequest);
        log.warn("@@ warn - loginRequest : {}", loginRequest);
        log.error("@@ error - username : {}, pw : {}", loginRequest.username(), loginRequest.password());

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
