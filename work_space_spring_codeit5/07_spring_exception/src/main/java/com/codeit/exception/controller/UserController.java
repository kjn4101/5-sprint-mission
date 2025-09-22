package com.codeit.exception.controller;

import com.codeit.exception.dto.ErrorResponse;
import com.codeit.exception.dto.UserDto;
import com.codeit.exception.exception.ErrorCode;
import com.codeit.exception.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@Controller
@RequestMapping("/users")
@Slf4j
public class UserController {

    @GetMapping("/form")
    public String userForm() {
        return "users/userForm";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UserDto user, Model model) {
        if (user.getUsername() == null || user.getPassword() == null) {
            throw new IllegalArgumentException("필수 값 누락");
        }

        if (user.getAge() < 0 || user.getAge() > 120) {
            throw new IllegalArgumentException("나이 범위 초과");
        }

        model.addAttribute("user", user);
        System.out.println(user);

        return "users/userView";
    }

    @GetMapping("/{username}")
    public String getUser(@PathVariable String username, Model model) throws AccessDeniedException {
        if (username.equals("unknown")) {
            throw new UserNotFoundException("사용자를 찾을 수 없습니다: " + username);
        }

        if (username.equals("admin")) {
            throw new AccessDeniedException("관리자 접근은 금지됨");
        }

        UserDto user = new UserDto();
        user.setUsername(username);
        user.setPassword("1234");
        user.setName("홍길동");
        user.setAge(30);
        model.addAttribute("user", user);
        return "users/userView";
    }

    @GetMapping("/admin/forbidden")
    public String forbidden() throws AccessDeniedException {
        throw new AccessDeniedException("접근 금지된 자원입니다.");
    }

    @GetMapping("/errorOrder")
    public String errorOrder() {
        int value = 1 / 0;
        return "users/userView";
    }

    // 예외처리
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException ex, Model model) {
        ex.printStackTrace();
        model.addAttribute("error", ErrorResponse.of(ErrorCode.INVALID_REQUEST, ex.getMessage()));
        return "error/errorPage";
    }

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFound(UserNotFoundException ex, Model model) {
        ex.printStackTrace();
        model.addAttribute("error", ErrorResponse.of(ErrorCode.USER_NOT_FOUND, ex.getMessage()));
        return "error/errorPage";
    }
    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDenied(AccessDeniedException ex, Model model) {
        ex.printStackTrace();
        model.addAttribute("error", ErrorResponse.of(ErrorCode.ACCESS_DENIED));
        return "error/errorPage";
    }


}
