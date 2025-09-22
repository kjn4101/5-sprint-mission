package com.codeit.start.controller;

import com.codeit.start.domain.User;
import com.codeit.start.repository.UserRepository;
import com.codeit.start.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Optional;


// @Controller 임을 알리는 어노테이션 = Bean이 될 대상
@Controller
@RequestMapping("/users") //  RequestMapping Controller에서 반복적으로 사용할 상위 url을 단축하는 방법
@RequiredArgsConstructor // lombok에서 필요한 생성자 만드는 방법
public class UserController {

    private final UserService userService;

    // @ RequiredArgsConstructor 때문에 생략이 가능
//    @Autowired
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }

    // 회원가입 페이지
    @GetMapping("/signup") // /users/signup
    public String signupForm() {
        return "users/signup-form";
    }


    // 회원가입 처리
    @PostMapping("/signup2")
    @ResponseBody // return 값을 body로 만들어주는 어노테이션
    public String  signup2(Long id, String username, String password, String name) {
        System.out.println(id);
        System.out.println(username);
        System.out.println(password);
        System.out.println(name);
        return "id : " +id + ", username@@@ : " + username + ", password : " + password;
    }


    // 회원가입 처리(User 객체로 간소화 시킨 버전)
    @PostMapping("/signup")
    public String signup(Model model, User user) {
        User registeredUser = userService.register(user);
        model.addAttribute("user", registeredUser);
        return "users/signup-success";
    }

    // 회원 전체 목록 페이지
    @GetMapping({"/", "/list"})
    public String listUsers(Model model){
        model.addAttribute("users", userService.getAllUsers());
        return "users/list";
    }

    // http://localhost:8081/users/{313}
    // 회원 단건 조회 페이지 - PathVariable
    @GetMapping("/{id}") // PathVariable 사용하는 방법
    public String showUser(@PathVariable Long id, Model model){
        Optional<User> user = userService.getUserById(id);
        if(user.isPresent()){
            model.addAttribute("user", user.get());
            return "users/detail";
        } else{
            model.addAttribute("message", "사용자를 찾을수 없습니다.");
            return "error/404";
        }
    }    

    // http://localhost:8081/users/find?id=1329
    // 회원 단건 조회 페이지 - param으로 받는 방법
    @GetMapping("/find")
    public String findUser(Long id, Model model){
        Optional<User> user = userService.getUserById(id);
        if(user.isPresent()){
            model.addAttribute("user", user.get());
            return "users/detail";
        } else{
            model.addAttribute("message", "사용자를 찾을수 없습니다.");
            return "error/404";
        }
    }

    // 회원 탈퇴
    @RequestMapping("/{id}/delete") // get, post 둘다 처리하는 방법
    public String deleteUser(@PathVariable Long id){
        Optional<User> user = userService.getUserById(id);
        user.orElseThrow(NoSuchElementException::new);
        userService.deleteUser(id);
        return "redirect:/users/"; // redirect view 반환이 아닌 브라우저가 페이지를 이동하는 명령
        // -> 전체 회원 목록으로 돌아가는 명령
    }

    // 모든 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.internalServerError().body("서버 오류가 발생했습니다");
    }

}
