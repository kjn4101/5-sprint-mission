package com.codeit.blog.intergration;

import com.codeit.blog.dto.user.UserCreateRequest;
import com.codeit.blog.dto.user.UserResponse;
import com.codeit.blog.service.UserService;
import com.codeit.blog.storage.FileStorage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UserApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserService userService;

    // 파일 IO 막기
    @MockitoBean
    private FileStorage fileStorage;

    @Test
    @DisplayName("사용자 생성 - 멀티파트(JSON part: user, 파일 part: avatar)")
    void createUser_success() throws Exception {
        // given
        var createReq = new UserCreateRequest(
                "user1",
                "password1",
                "user1@email.com",
                "홍길동",
                LocalDate.of(1990, 1, 1)
        );

        MockMultipartFile userPart = new MockMultipartFile(
                "user", "", MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(createReq)
        );
        MockMultipartFile avatarPart = new MockMultipartFile(
                "avatar", "avatar.jpg", MediaType.IMAGE_JPEG_VALUE,
                "dummy-image".getBytes()
        );

        // when & then
        mockMvc.perform(multipart("/api/v1/users")
                        .file(userPart)
                        .file(avatarPart)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                // ApiResult 래핑
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id", notNullValue()))
                .andExpect(jsonPath("$.data.username").value("user1"))
                .andExpect(jsonPath("$.data.email").value("user1@email.com"));
    }

    @Test
    @DisplayName("전체 조회 - 특정 사용자들이 포함되어 있는지 확인")
    void findAllUsers_success() throws Exception {
        // given: 두 명 생성
        userService.create(new UserCreateRequest(
                "user2", "password1!", "user2@email.com", "김길동", LocalDate.of(1992, 2, 2)
        ), null);
        userService.create(new UserCreateRequest(
                "user3", "password1!", "user3@email.com", "임길동", LocalDate.of(1993, 3, 3)
        ), null);

        // when & then
        mockMvc.perform(get("/api/v1/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data..username", hasItems("user2", "user3")));
    }

    @Test
    @DisplayName("사용자 삭제 - 성공 후 메시지 확인")
    void deleteUser_success() throws Exception {
        // given: 한 명 생성
        UserResponse created = userService.create(new UserCreateRequest(
                "user4", "password1!", "user4@email.com", "삭제대상", LocalDate.of(1994, 4, 4)
        ), null);

        // when & then
        mockMvc.perform(delete("/api/v1/users/{id}", created.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("사용자가 삭제되었습니다."));
    }
}