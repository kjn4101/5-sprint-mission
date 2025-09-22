package com.codeit.web.controller;


import com.codeit.web.dto.UserDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;

@Controller
public class LoginController {


    @GetMapping("/login")
    public String loginForm(@CookieValue(value = "savedUsername", required = false) String savedUsername) {
        System.out.println("저장된 cookie 값 : " + savedUsername);

        return "users/loginForm";
    }


    @PostMapping("/login")
    public String login(@ModelAttribute UserDto userDto,
                        HttpServletResponse response,
                        HttpSession session
    ) {
        // username을 쿠키로 저장
        Cookie cookie = new Cookie("savedUsername", userDto.getUsername());
//        cookie.setMaxAge(60 * 60 * 24 * 30); //
//        cookie.setMaxAge(60 * 60 * 24 * 7); // 7일 유지
        cookie.setMaxAge(20); // 20초 유지
        cookie.setPath("/");
//        cookie.setHttpOnly(true); // 서버에서만 쿠키를 사용할때 옵션
//        cookie.setSecure(true); // 지금은 걸면 안된다! -> https에서만 활용 가능!
        response.addCookie(cookie);

        // 세션에 로그인된 정보 저장
        session.setAttribute("loginUser", userDto);
//        session.setMaxInactiveInterval(3);
//        return "users/loginSuccess";
        return "redirect:/login/success";
    }


    @GetMapping("/login/success")
    public String loginSuccess(HttpSession session, Model model) {
        UserDto user = (UserDto) session.getAttribute("loginUser");
        model.addAttribute("user", user);
        return "users/loginSuccess";
    }

    @GetMapping("/login/status")
    public String loginStatus(Model model, @SessionAttribute(name = "loginUser", required = false) UserDto user,HttpSession session) {
        System.out.println("MaxInactiveInterval : " + session.getMaxInactiveInterval());
        System.out.println("user : " + user);
        model.addAttribute("user", user);
        return "users/loginStatus";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletResponse response) {
        // 세션에서 사용자 제거하기
        System.out.println(session.getId());
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(session.getCreationTime()));
        session.removeAttribute("loginUser"); // 하나만 제거할때 활용!
        session.invalidate(); // 세션 자체를 삭제할때, 실제 로그아웃할때는 이걸 활용!

        // Cookie savedUsername 제거하기
        Cookie cookie = new Cookie("savedUsername", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/login";
    }



}





