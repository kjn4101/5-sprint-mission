package com.codeit.start.controller;


import com.codeit.start.domain.User;
import com.codeit.start.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

// rest 컨트롤러
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserControllerForRest {

    private final UserService userService;
    
    
    // 회원 전체 목록
    // ResponseEntity : 응답 값을 만들고 JSON으로 변환시켜주는 객체
    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers() {
//        return ResponseEntity.ok(userService.getAllUsers());
        return ResponseEntity.status(200).body(userService.getAllUsers());
    }
    
    // 회원 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        user.orElseThrow(NoSuchElementException::new);
        return ResponseEntity.ok(user.get());
    }

    // 회원 등록
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) { // @RequestBody : json body 파싱할때
        User savedUser = userService.register(user);
        return ResponseEntity.status(201).body(savedUser);
    }

    // 회원 등록
    @PostMapping("/create")
    public ResponseEntity<User> createFormUser(User user) { // form으로 받을때
        User savedUser = userService.register(user);
        return ResponseEntity.status(201).body(savedUser);
    }

    // 회원 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        user.orElseThrow(NoSuchElementException::new);
        userService.deleteUser(id);
        return ResponseEntity.ok(user.get());
    }

    // 모든 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.internalServerError().body("서버 오류가 발생했습니다");
    }
}
