package com.codeit.blog.intergration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.codeit.blog.dto.base.ApiResult;
import com.codeit.blog.dto.user.UserCreateRequest;
import com.codeit.blog.dto.user.UserLoginRequest;
import com.codeit.blog.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // spring context 환경에서 테스트
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class AuthApiIntegrationTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired private ObjectMapper objectMapper;
	@Autowired private UserService userService;

	@Autowired  private TestRestTemplate restTemplate;
	@LocalServerPort  private int port;

	@Test
	@DisplayName("로그인 성공")
	void login_success() throws Exception {
		UserCreateRequest createReq = new UserCreateRequest(
			"loginuser",
			"password1!",
			"login@example.com",
			"스프링마스터",
			LocalDate.of(1990, 1, 1)
		);
		userService.create(createReq, null);

		UserLoginRequest loginReq = new UserLoginRequest("loginuser", "password1!");
		String body = objectMapper.writeValueAsString(loginReq);

		mockMvc.perform(post("/api/v1/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.data.username").value("loginuser"));
	}


	@Test
	@DisplayName("로그인 성공 - restTemplate")
	void login_success_restTemplate() throws Exception {
		UserCreateRequest createReq = new UserCreateRequest(
			"loginuser",
			"password1!",
			"login@example.com",
			"스프링마스터",
			LocalDate.of(1990, 1, 1)
		);
		userService.create(createReq, null);

		UserLoginRequest loginReq = new UserLoginRequest("loginuser", "password1!");

		String url = "http://localhost:" + port + "/api/v1/auth/login";

		// 요청 방법
		ResponseEntity<ApiResult> result = restTemplate.postForEntity(url, loginReq, ApiResult.class);
		System.out.println("@@@" + result.toString());
	}


	@Test
	@DisplayName("로그인 API - 실패(존재하지 않는 사용자) → 404")
	void login_userNotFound() throws Exception {
		UserLoginRequest loginReq = new UserLoginRequest("no_such_user", "Password1!");
		String body = objectMapper.writeValueAsString(loginReq);

		mockMvc.perform(post("/api/v1/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.code").value("USER_NOT_FOUND"));
	}

	@Test
	@DisplayName("로그인 API - 실패(잘못된 비밀번호) → 401")
	void login_invalidPassword() throws Exception {
		// Given: 사용자 생성
		UserCreateRequest createReq = new UserCreateRequest(
			"loginuser2",
			"Password1!",
			"login2@example.com",
			"nick2",
			LocalDate.of(1992, 2, 2)
		);
		userService.create(createReq, null);

		// Wrong password
		UserLoginRequest loginReq = new UserLoginRequest("loginuser2", "WrongPassword!");
		String body = objectMapper.writeValueAsString(loginReq);

		mockMvc.perform(post("/api/v1/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
			.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("$.code").value("INVALID_USER_CREDENTIALS"));
	}

	@Test
	@DisplayName("로그인 API - 실패(유효하지 않은 요청) → 400")
	void login_invalidRequest() throws Exception {
		// @Valid 위반 유발
		UserLoginRequest invalid = new UserLoginRequest("", "");
		String body = objectMapper.writeValueAsString(invalid);

		mockMvc.perform(post("/api/v1/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
	}




}