package com.codeit.exception.controller;

import com.codeit.exception.dto.UserDto;
import com.codeit.exception.exception.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("api/users")
public class UserControllerForRest {

    // 테스트용 POST: 사용자 등록
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDto userDto) {
        if (userDto.getUsername() == null || userDto.getPassword() == null) {
            throw new IllegalArgumentException("필수 값 누락");
        }

        if (userDto.getAge() < 0 || userDto.getAge() > 120) {
            throw new IllegalArgumentException("나이 범위 초과");
        }

        return ResponseEntity.ok("등록 완료: " + userDto.getUsername());
    }

    // 테스트용 GET: 사용자 조회
    @GetMapping("/{username}")
    public ResponseEntity<UserDto> getUser(@PathVariable String username) {
        if (username.equals("unknown")) {
            throw new UserNotFoundException("사용자를 찾을 수 없습니다: " + username);
        }

        if (username.equals("admin")) {
            throw new RuntimeException("관리자 접근은 금지됨");
        }

        UserDto user = new UserDto();
        user.setUsername(username);
        user.setPassword("1234");
        user.setName("홍길동");
        user.setAge(30);
        return ResponseEntity.ok(user);
    }

    // 테스트용 GET: 접근 거부
    @GetMapping("/admin/forbidden")
    public String accessDenied() throws AccessDeniedException {
        throw new AccessDeniedException("접근 금지된 자원입니다.");
    }
}
