package com.sprint.mission.discodeit.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicAuthService;

@ExtendWith(MockitoExtension.class)
class BasicAuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private BasicAuthService basicAuthService;

    private User user;
    private UserDto userDto;
    private LoginRequest loginRequest;
    private String username;
    private String password;

    @BeforeEach
    void setUp() {
        username = "testuser";
        password = "testpassword";
        
        user = new User(username, "test@example.com", password, null);
        userDto = new UserDto(UUID.randomUUID(), username, "test@example.com", null, false);
        loginRequest = new LoginRequest(username, password);
    }

    @Test
    @DisplayName("로그인 성공 - 정상적인 사용자명과 비밀번호")
    void login_Success() {
        // given
        given(userRepository.findByUsername(username)).willReturn(Optional.of(user));
        given(userMapper.toDto(user)).willReturn(userDto);

        // when
        UserDto result = basicAuthService.login(loginRequest);

        // then
        assertThat(result).isNotNull();
        assertThat(result.username()).isEqualTo(username);
        
        then(userRepository).should().findByUsername(username);
        then(userMapper).should().toDto(user);
    }

    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 사용자")
    void login_Fail_UserNotFound() {
        // given
        given(userRepository.findByUsername(username)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> basicAuthService.login(loginRequest))
            .isInstanceOf(UserNotFoundException.class);

        then(userRepository).should().findByUsername(username);
        then(userMapper).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 비밀번호")
    void login_Fail_WrongPassword() {
        // given
        String wrongPassword = "wrongpassword";
        LoginRequest wrongLoginRequest = new LoginRequest(username, wrongPassword);
        
        given(userRepository.findByUsername(username)).willReturn(Optional.of(user));

        // when & then
        assertThatThrownBy(() -> basicAuthService.login(wrongLoginRequest))
            .isInstanceOf(DiscodeitException.class)
            .hasMessageContaining("Wrong password");

        then(userRepository).should().findByUsername(username);
        then(userMapper).shouldHaveNoInteractions();
    }
}