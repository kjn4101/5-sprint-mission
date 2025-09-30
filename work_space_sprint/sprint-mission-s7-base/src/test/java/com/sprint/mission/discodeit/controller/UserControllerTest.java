package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;

@WebMvcTest(UserController.class)
@DisplayName("UserController 슬라이스 테스트")
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private UserService userService;

  @MockitoBean
  private UserStatusService userStatusService;

  @Test
  @DisplayName("사용자 생성 - 성공")
  void createUser_Success() throws Exception {
    // given
    UserCreateRequest request = new UserCreateRequest(
        "testuser",
        "test@example.com",
        "password123"
    );

    UUID userId = UUID.randomUUID();
    UserDto response = new UserDto(
        userId,
        "testuser",
        "test@example.com",
        null,
        true
    );

    String requestJson = objectMapper.writeValueAsString(request);
    MockMultipartFile userPart = new MockMultipartFile(
        "userCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        requestJson.getBytes()
    );

    given(userService.create(eq(request), any())).willReturn(response);

    // when & then
    mockMvc.perform(multipart("/api/users")
            .file(userPart)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(userId.toString()))
        .andExpect(jsonPath("$.username").value("testuser"))
        .andExpect(jsonPath("$.email").value("test@example.com"));
  }

  @Test
  @DisplayName("사용자 생성 - 유효성 검증 실패 (username 누락)")
  void createUser_ValidationFailed() throws Exception {
    // given
    UserCreateRequest request = new UserCreateRequest(
        "",  // 빈 사용자명
        "test@example.com",
        "password123"
    );

    String requestJson = objectMapper.writeValueAsString(request);
    MockMultipartFile userPart = new MockMultipartFile(
        "userCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        requestJson.getBytes()
    );

    // when & then
    mockMvc.perform(multipart("/api/users")
            .file(userPart)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("VALIDATION_FAILED"));
  }

  @Test
  @DisplayName("사용자 수정 - 성공")
  void updateUser_Success() throws Exception {
    // given
    UUID userId = UUID.randomUUID();
    UserUpdateRequest request = new UserUpdateRequest(
        "newusername",
        "newemail@example.com",
        "newpassword123"
    );

    UserDto response = new UserDto(
        userId,
        "newusername",
        "newemail@example.com",
        null,
        true
    );

    String requestJson = objectMapper.writeValueAsString(request);
    MockMultipartFile userPart = new MockMultipartFile(
        "userUpdateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        requestJson.getBytes()
    );

    given(userService.update(eq(userId), eq(request), any())).willReturn(response);

    // when & then
    mockMvc.perform(multipart("/api/users/{userId}", userId)
            .file(userPart)
            .with(req -> {
              req.setMethod("PATCH");
              return req;
            })
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("newusername"))
        .andExpect(jsonPath("$.email").value("newemail@example.com"));
  }

  @Test
  @DisplayName("사용자 수정 - 사용자 없음")
  void updateUser_UserNotFound() throws Exception {
    // given
    UUID userId = UUID.randomUUID();
    UserUpdateRequest request = new UserUpdateRequest(
        "newusername",
        "newemail@example.com",
        "newpassword123"
    );

    String requestJson = objectMapper.writeValueAsString(request);
    MockMultipartFile userPart = new MockMultipartFile(
        "userUpdateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        requestJson.getBytes()
    );

    given(userService.update(eq(userId), eq(request), any()))
        .willThrow(new UserNotFoundException(userId));

    // when & then
    mockMvc.perform(multipart("/api/users/{userId}", userId)
            .file(userPart)
            .with(req -> {
              req.setMethod("PATCH");
              return req;
            })
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.exceptionType").value("UserNotFoundException"));  // 수정
  }

  @Test
  @DisplayName("사용자 삭제 - 성공")
  void deleteUser_Success() throws Exception {
    // given
    UUID userId = UUID.randomUUID();
    doNothing().when(userService).delete(userId);

    // when & then
    mockMvc.perform(delete("/api/users/{userId}", userId))
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("사용자 삭제 - 사용자 없음")
  void deleteUser_UserNotFound() throws Exception {
    // given
    UUID userId = UUID.randomUUID();
    doThrow(new UserNotFoundException(userId))
        .when(userService).delete(userId);

    // when & then
    mockMvc.perform(delete("/api/users/{userId}", userId))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.exceptionType").value("UserNotFoundException"));  // 수정
  }

  @Test
  @DisplayName("전체 사용자 목록 조회 - 성공")
  void findAllUsers_Success() throws Exception {
    // given
    List<UserDto> users = List.of(
        new UserDto(UUID.randomUUID(), "user1", "user1@example.com", null, true),
        new UserDto(UUID.randomUUID(), "user2", "user2@example.com", null, false)
    );

    given(userService.findAll()).willReturn(users);

    // when & then
    mockMvc.perform(get("/api/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].username").value("user1"))
        .andExpect(jsonPath("$[1].username").value("user2"));
  }
}