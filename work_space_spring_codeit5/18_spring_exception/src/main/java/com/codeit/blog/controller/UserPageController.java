package com.codeit.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserPageController {

    @GetMapping("/user/signup")
    public String signupPage() {
        return "user/signup";
    }

    @GetMapping("/user/signup2")
    public String signupPage2() {
        return "user/signup2";
    }

    @GetMapping("/user/update")
    public String updatePage() {
        return "user/update";
    }

    @GetMapping("/user/delete")
    public String deletePage() {
        return "user/delete";
    }

    @GetMapping("/user/login")
    public String loginPage() {
        return "user/login";
    }

    @GetMapping("/user/login2")
    public String loginPage2() {
        return "user/login2";
    }

    @GetMapping("/user/find")
    public String findPage() {
        return "user/find";
    }
}
