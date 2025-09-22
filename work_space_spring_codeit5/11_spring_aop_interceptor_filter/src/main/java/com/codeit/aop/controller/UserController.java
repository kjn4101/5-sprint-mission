package com.codeit.aop.controller;

import com.codeit.aop.dto.user.UserCreateRequest;
import com.codeit.aop.entity.User;
import com.codeit.aop.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	/** 로그인 페이지 */
	@RequestMapping("/loginPage")
	public String loginPage() {
		return "user/login";
	}

	/** 로그인 처리 (username/password 비교) */
	@RequestMapping("/login")
	public String login(Model model,
						@RequestParam String username,
						@RequestParam String password,
						HttpSession session) {
		try {
			User found = userService.findByUsername(username);
			if (found != null && found.getPassword() != null && found.getPassword().equals(password)) {
				session.setAttribute("loginUser", found);
				return "user/loginSuccess";
			}
			// 비밀번호 불일치
			return "user/loginFailed";
		} catch (NoSuchElementException e) {
			e.printStackTrace();
			// 사용자 없음
			return "user/loginFailed";
		}
	}

	/** 회원가입 페이지 (register) */
	@RequestMapping("/registerPage")
	public String registerPage() {
		return "user/register"; // templates/user/register.html
	}

	/** 회원가입 처리 (DTO → Service.create) */
	@RequestMapping("/register")
	public String register(Model model, @ModelAttribute UserCreateRequest request) {
		try {
			User created = userService.create(request);
			model.addAttribute("user", created);
			return "user/registerSuccess";
		} catch (IllegalArgumentException e) {
			model.addAttribute("error", e.getMessage());
			return "user/registerFailed";
		}
	}

	/** 아이디(=username) 중복 체크: 중복이면 "1", 아니면 "0" */
	@ResponseBody
	@RequestMapping(value = "/checkId", produces = "text/html; charset=utf-8")
	public String checkId(@RequestParam String username) {
		try {
			userService.findByUsername(username);
			return "1"; // 존재(중복)
		} catch (NoSuchElementException e) {
			return "0"; // 미존재
		}
	}

	/** 사용자 목록 페이지 (AJAX로 목록 로드) */
	@RequestMapping("/userList")
	public String userList() {
		return "user/list";
	}

	/** 사용자 전체 목록 JSON */
	@ResponseBody
	@RequestMapping(value = "/userAllList", produces = "application/json; charset=utf-8")
	public List<User> userAllList() {
		return userService.findAll(); // 스프링이 자동으로 JSON 변환
	}

	/** 에러 페이지 */
	@RequestMapping("/error-page")
	public String error() {
		return "common/error";
	}

	/** 공통 예외 처리 */
	@ExceptionHandler(RuntimeException.class)
	public String errorHandler() {
		return "redirect:/error-page";
	}
}
