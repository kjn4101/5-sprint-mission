package com.codeit.di.controller;

import com.codeit.di.domain.User;
import com.codeit.di.dto.UserLoginRequest;
import com.codeit.di.dto.UserLoginResponse;
import com.codeit.di.dto.UserRegisterRequest;
import com.codeit.di.dto.UserRegisterResponse;
import com.codeit.di.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/users")
//@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserService adminService;

    // @Primary 때문에 UserService 주입이 모두 userService가 되는 현상이 있을때 적용해야하는 코드
    public UserController(@Qualifier("userService")  UserService userService,
                          @Qualifier("adminService") UserService adminService) {
        this.userService = userService;
        this.adminService = adminService;
    }


    // 로그인 페이지
    @GetMapping("/login")
    public String loginForm() {
        return "users/login-form";
    }

    // 로그인
    @PostMapping("/login")
    public String login(Model model, UserLoginRequest request) {
        System.out.println(request);

        userService.logout();
        adminService.logout();

        UserService selectedService;
        if(request.getRoles().equals("user")) {
            selectedService = userService;
        }else{
            selectedService = adminService;
        }

        UserLoginResponse response = selectedService.login(request);
        model.addAttribute("response", response);
        return "users/login-success";
    }

    // 로그아웃
    @RequestMapping("/logout")
    public String logout() {
        userService.logout();
        adminService.logout();
        return "redirect:/";
    }

    // 회원가입 페이지
    @GetMapping("/signup")
    public String signupForm() {
        return "users/signup-form";
    }


    // 회원 가입 처리
    @PostMapping("/signup")
    public String signup(Model model, UserRegisterRequest request) {
        UserRegisterResponse response =  userService.register(request);
        model.addAttribute("response", response);
        return "users/signup-success";
    }

    // 회원 전체 목록 페이지
    @GetMapping({"/list", "/"})
    public String list(Model model) {
        // 로그인 사용자 정보 전달하기
        Optional<User> loginUser = userService.getLoginUser();
        Optional<User> adminLoginUser = adminService.getLoginUser();
        User user = loginUser.orElse(adminLoginUser.orElse(null));
        if(user != null) {
            user.setPassword(null);
        }
        model.addAttribute("loginUser", user);

        if(adminLoginUser.isPresent()) {
            model.addAttribute("users", adminService.getAllUsers());
        }else{
            model.addAttribute("users", userService.getAllUsers());
        }
        return "users/list";
    }

    // 유저 단건 조회
    @GetMapping("/find")
    public String detail(Model model, Long id) {
        // 로그인 한 사용자 정보
        Optional<User> loginUser = userService.getLoginUser();
        Optional<User> adminLoginUser = adminService.getLoginUser();

        boolean isAdmin = adminLoginUser.isPresent();
        boolean isOwner = loginUser.isPresent() && loginUser.get().getId().equals(id);

        if(!isAdmin && !isOwner) {
            // 접근 권한 없음!!
            return "error/404";
        }

        // 사용자 조회
        Optional<User> user = isAdmin ? adminService.getUserById(id) : userService.getUserById(id);
        if(user.isPresent()) {
            model.addAttribute("user", user.get());
            return "users/detail";
        }else{
            return "error/404";
        }
    }

    // 회원 삭제
    @RequestMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        // 로그인한 사용자 정보
        Optional<User> loginUser = userService.getLoginUser();
        Optional<User> adminUser = adminService.getLoginUser();

        // 로그인 안했거나, 관리자가 아니고 본인도 아니면 접근 불가
        boolean isAdmin = adminUser.isPresent();
        boolean isOwner = loginUser.isPresent() && loginUser.get().getId().equals(id);

        if (!isAdmin && !isOwner) {
            return "error/404";  // 접근 권한 없음
        }

        // 삭제 처리
        userService.deleteUser(id);
        return "redirect:/users/logout";
    }
}
