package com.codeit.web.controller;

import com.codeit.web.dto.UserDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/users")
public class LoginRestController {

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody UserDto userDto, HttpSession session) {
        session.setAttribute("loginUser", userDto);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/loginForm")
    public ResponseEntity<UserDto> loginForm(UserDto userDto, HttpSession session) {
        session.setAttribute("loginUser", userDto);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getLoginUser(HttpSession session) {
        UserDto loginUser = (UserDto) session.getAttribute("loginUser");
        if (loginUser == null) {
            return ResponseEntity.status(401).build();  // 인증 안 된 경우
        }
        return ResponseEntity.ok(loginUser);
    }

    @GetMapping("/logout")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();  // 세션 전체 초기화
        return ResponseEntity.ok("로그아웃 완료");
    }
}
