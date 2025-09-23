package com.sprint.mission.discodeit;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;

@ExtendWith(MockitoExtension.class)
class BasicUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserStatusRepository userStatusRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private BinaryContentRepository binaryContentRepository;

    @Mock
    private BinaryContentStorage binaryContentStorage;

    @InjectMocks
    private BasicUserService userService;

    private User mockUser;
    private UserDto userDto;
    private UserCreateRequest userCreateRequest;
    private UserUpdateRequest userUpdateRequest;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        mockUser = new User("testuser", "test@example.com", "password", null);
        userDto = new UserDto(userId, "testuser", "test@example.com", null, false);
        userCreateRequest = new UserCreateRequest("testuser", "test@example.com", "password123");
        userUpdateRequest = new UserUpdateRequest("newuser", "new@example.com", "newpassword123");
    }

    @Test
    @DisplayName("사용자 생성 - 성공")
    void create_Success() {
        // given
        given(userRepository.existsByEmail("test@example.com")).willReturn(false);
        given(userRepository.existsByUsername("testuser")).willReturn(false);
        given(userRepository.save(any(User.class))).willReturn(mockUser);
        given(userMapper.toDto(any(User.class))).willReturn(userDto);

        // when
        UserDto result = userService.create(userCreateRequest, Optional.empty());

        // then
        assertThat(result).isNotNull();
        assertThat(result.username()).isEqualTo("testuser");
        assertThat(result.email()).isEqualTo("test@example.com");

        then(userRepository).should().existsByEmail("test@example.com");
        then(userRepository).should().existsByUsername("testuser");
        then(userRepository).should().save(any(User.class));
    }

    @Test
    @DisplayName("사용자 생성 - 실패: 이메일 중복")
    void create_Fail_EmailAlreadyExists() {
        // given
        given(userRepository.existsByEmail("test@example.com")).willReturn(true);

        // when & then
        assertThatThrownBy(() -> userService.create(userCreateRequest, Optional.empty()))
            .isInstanceOf(UserAlreadyExistsException.class);

        then(userRepository).should().existsByEmail("test@example.com");
        then(userRepository).should(times(0)).save(any(User.class));
    }

    @Test
    @DisplayName("사용자 수정 - 성공")
    void update_Success() {
        // given
        given(userRepository.findById(userId)).willReturn(Optional.of(mockUser));
        given(userRepository.existsByEmail("new@example.com")).willReturn(false);
        given(userRepository.existsByUsername("newuser")).willReturn(false);
        given(userMapper.toDto(any(User.class))).willReturn(userDto);

        // when
        UserDto result = userService.update(userId, userUpdateRequest, Optional.empty());

        // then
        assertThat(result).isNotNull();

        then(userRepository).should().findById(userId);
        then(userRepository).should().existsByEmail("new@example.com");
        then(userRepository).should().existsByUsername("newuser");
    }

    @Test
    @DisplayName("사용자 수정 - 실패: 사용자 없음")
    void update_Fail_UserNotFound() {
        // given
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.update(userId, userUpdateRequest, Optional.empty()))
            .isInstanceOf(UserNotFoundException.class);

        then(userRepository).should().findById(userId);
    }

    @Test
    @DisplayName("사용자 삭제 - 성공")
    void delete_Success() {
        // given
        given(userRepository.existsById(userId)).willReturn(true);

        // when
        userService.delete(userId);

        // then
        then(userRepository).should().existsById(userId);
        then(userRepository).should().deleteById(userId);
    }

    @Test
    @DisplayName("사용자 삭제 - 실패: 사용자 없음")
    void delete_Fail_UserNotFound() {
        // given
        given(userRepository.existsById(userId)).willReturn(false);

        // when & then
        assertThatThrownBy(() -> userService.delete(userId))
            .isInstanceOf(UserNotFoundException.class);

        then(userRepository).should().existsById(userId);
        then(userRepository).should(times(0)).deleteById(userId);
    }
}