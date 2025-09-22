package com.codeit.blog.controller;


import com.codeit.blog.dto.user.UserLoginRequest;
import com.codeit.blog.dto.user.UserResponse;
import com.codeit.blog.exception.user.InvalidCredentialsException;
import com.codeit.blog.exception.user.UserNotFoundException;
import com.codeit.blog.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// MVC 테스트를 위한 어노테이션
@WebMvcTest(controllers = AuthController.class) // mvc(http 재연용)을 사용하기 위한 어노테이션
@AutoConfigureMockMvc(addFilters = false) // MockMvc 설정을 도와주는 어노테이션
public class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;  // http 요청과 응답을 처리해줄 mock 객체

	@Autowired
	private ObjectMapper objectMapper; // json 처리를 도와줄 Mapper

	@MockitoBean
	private AuthService authService;
	@MockitoBean
	private JpaMetamodelMappingContext jpaMetamodelMappingContext; // 가상의 jpa을 실행해줄 환경


	@Test
	@DisplayName("로그인 성공 - 200 + success + username 검증")
	void login_success_200_username() throws Exception {
		UserLoginRequest userLoginRequest = new UserLoginRequest("test01", "password123");
		String request = objectMapper.writeValueAsString(userLoginRequest); // json 변환

		// 응답 설계
		UserResponse response = new UserResponse(1L, "test01",
			"password123", "test01@email.com",
			"스프링마스터",
			LocalDate.of(1990, 5, 21), Instant.now(), Instant.now());

		given(authService.login(any())).willReturn(response);

		mockMvc.perform(post("/api/v1/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.data.username").value("test01"));
	}

	@Test
	@DisplayName("로그인 실패 - 404(존재하지 않는 사용자)")
	void login_fail_not_found() throws Exception {
		UserLoginRequest userLoginRequest = new UserLoginRequest("no_such", "password123");
		String request = objectMapper.writeValueAsString(userLoginRequest); // json 변환

		given(authService.login(any())).willThrow(UserNotFoundException.withUsername("no_such"));

		mockMvc.perform(post("/api/v1/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.code").value("USER_NOT_FOUND"));

	}

	@Test
	@DisplayName("로그인 실패 - 400(검증에러)")
	void login_fail_bad_request() throws Exception {
		// @Valid 위반 유발
		UserLoginRequest userLoginRequest = new UserLoginRequest("", "");
		String request = objectMapper.writeValueAsString(userLoginRequest);

		mockMvc.perform(post("/api/v1/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
	}

	@Test
	@DisplayName("로그인 실패 - 비밀번호 불일치")
	void login_invalidPassword_light() throws Exception {
		UserLoginRequest req = new UserLoginRequest("test01", "wrong");
		String body = objectMapper.writeValueAsString(req);

		given(authService.login(any(UserLoginRequest.class)))
			.willThrow(InvalidCredentialsException.wrongPassword());

		mockMvc.perform(post("/api/v1/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
			.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("$.code").value("INVALID_USER_CREDENTIALS"));
	}

}