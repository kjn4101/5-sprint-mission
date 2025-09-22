package com.codeit.blog.service;


import com.codeit.blog.dto.user.UserCreateRequest;
import com.codeit.blog.dto.user.UserResponse;
import com.codeit.blog.dto.user.UserUpdateRequest;
import com.codeit.blog.entity.User;
import com.codeit.blog.exception.user.UserAlreadyExistsException;
import com.codeit.blog.exception.user.UserNotFoundException;
import com.codeit.blog.mapper.UserMapper;
import com.codeit.blog.repository.UserRepository;
import com.codeit.blog.storage.FileStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;



// mock을 통한 테스트 수행 코드
@ExtendWith(MockitoExtension.class) // MockitoExtension 환경에서 테스트 하는 어노테이션
public class UserServiceTest {
    // UserService를 테스트할때 필요한 의존성을 Mock으로 만드는 영역
    @Mock // 해당 객체를 실제 객체가 아닌 Mock으로 만들어 활용할때 사용하는 어노테이션
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private FileStorage fileStorage;

    @InjectMocks // 선언 된 @Mock 객체 기반으로 자동으로 주입하는 어노테이션
    private UserService userService;

    // 테스트에 활용될 객체나 변수들 선언
    private Long userId;
    private String username;
    private String email;
    private String password;

    private User user;                 // 매핑된 엔티티(저장 전)
    private UserResponse userResponse; // 서비스 반환 DTO

    @BeforeEach
    void setUp(){
        userId = 1L;
        username = "test01";
        email = "test01@email.com";
        password = "password1234";

        user = User.builder()
            .username(username)
            .email(email)
            .password(password)
            .nickname("스프링마스터")
            .birthday(LocalDate.of(1990, 1, 1))
            .hasAvatar(false)
            .build();

        userResponse = new UserResponse(
            userId,
            username,
            password,
            email,
            "스프링마스터",
            LocalDate.of(1990, 1, 1),
            Instant.now(),
            Instant.now());
    }

    @Test
    @DisplayName("사용자 생성 테스트(아바타 없음)")
    void create_user_without_avatar(){
        // given
        UserCreateRequest req = new UserCreateRequest(
            username, password, email, "스프링마스터",
            LocalDate.of(1990, 1, 1));

        // given 모의 설정(Mock 시나리오 설정)
        // User user = userMapper.toUser(newUser);
        given(userMapper.toUser(any())).willReturn(user);

        // userRepository.existsByUsername(user.getUsername())
        given(userRepository.existsByUsername(username)).willReturn(false);

        // userRepository.save(user);
        given(userRepository.save(any())).willReturn(user);

        // userMapper.toUserDetailResponse(user);
        given(userMapper.toUserDetailResponse(any())).willReturn(userResponse);

        // when
        UserResponse result = userService.create(req, null);

        // then
        assertThat(result).isEqualTo(userResponse);
        verify(userRepository, times(1)).save(any());
        verify(userMapper, times(1)).toUser(any());
    }

    @Test
    @DisplayName("사용자 생성 테스트(아바타 있음)")
    void create_user_withAvatar(){
        // given
        UserCreateRequest req = new UserCreateRequest(
            username, password, email, "스프링마스터",
            LocalDate.of(1990, 1, 1));
        MultipartFile avatar = new MockMultipartFile(
            "avatar", "a.png", "image/png", "png".getBytes());

        // given 모의 설정(Mock 시나리오 설정)
        // User user = userMapper.toUser(newUser);
        given(userMapper.toUser(any())).willReturn(user);

        // userRepository.existsByUsername(user.getUsername())
        given(userRepository.existsByUsername(username)).willReturn(false);

        // userRepository.save(user);
        given(userRepository.save(any())).willReturn(user);

        // userMapper.toUserDetailResponse(user);
        given(userMapper.toUserDetailResponse(any())).willReturn(userResponse);

        // when
        UserResponse result = userService.create(req, avatar);

        // then
        assertThat(result).isEqualTo(userResponse);
        verify(userRepository, times(1)).save(any());
        verify(userMapper, times(1)).toUser(any());

        // save로직이 동작했는지 검증
        verify(fileStorage).saveAvatarFile(eq(username), any(MultipartFile.class));
    }

    @Test
    @DisplayName("생성 실패 : username 중복")
    void create_fail_duplicateUsername(){
        // given
        UserCreateRequest req = new UserCreateRequest(username, password, email, "스프링마스터", LocalDate.of(1990, 1, 1));
        given(userMapper.toUser(any())).willReturn(user);
        given(userRepository.existsByUsername(username)).willReturn(true); // 중복

        // when & then
        assertThatThrownBy(()-> userService.create(req, null))
            .isInstanceOf(UserAlreadyExistsException.class);
    }

    // 조회
    @Test
    @DisplayName("findById 성공")
    void findById_success(){
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(userMapper.toUserDetailResponse(any())).willReturn(userResponse);

        UserResponse result = userService.findById(userId);

        assertThat(result).isEqualTo(userResponse);
    }

    // 조회 실패
    @Test
    @DisplayName("findById 실패 (사용자 없음)")
    void findById_fail_notfound(){
        given(userRepository.findById(userId)).willReturn(Optional.empty());
        assertThatThrownBy(()-> userService.findById(userId))
            .isInstanceOf(UserNotFoundException.class);
    }


    @Test
    @DisplayName("findAll: 전체 조회")
    void findAll_success() {
        List<User> users = List.of(user);
        List<UserResponse> responses = List.of(userResponse);

        given(userRepository.findAll()).willReturn(users);
        given(userMapper.toUserResponseList(users)).willReturn(responses);

        assertThat(userService.findAll()).isEqualTo(responses);
    }

    // 수정
    @Test
    @DisplayName("update 성공(아바타 없음)")
    void update_success_without_avatar(){
        UserUpdateRequest req = new UserUpdateRequest("newPassword",
            "new@email.com", "스프링초보자", LocalDate.of(1990, 1, 1));

        given(userRepository.findById(any())).willReturn(Optional.of(user));
        given(userRepository.save(any())).willReturn(user);
        given(userMapper.toUserDetailResponse(any())).willReturn(userResponse);

        UserResponse result = userService.update(userId, req, null);

        assertThat(result).isEqualTo(userResponse);
    }


    @Test
    @DisplayName("update 성공(아바타 갱신 포함)")
    void update_success_withAvatar() {
        UserUpdateRequest patch = new UserUpdateRequest(
            "newPass!", "new@email.com", "newNick", LocalDate.of(1991, 2, 2)
        );
        MultipartFile avatar = new MockMultipartFile("avatar", "a.png", "image/png", "png".getBytes());

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(userRepository.save(any(User.class))).willAnswer(inv -> inv.getArgument(0));
        given(userMapper.toUserDetailResponse(any(User.class))).willReturn(userResponse);

        UserResponse result = userService.update(userId, patch, avatar);

        assertThat(result).isEqualTo(userResponse);
        verify(fileStorage).saveAvatarFile(eq(username), any(MultipartFile.class));
    }

    @Test
    @DisplayName("update 실패: 사용자 없음")
    void update_notFound() {
        UserUpdateRequest patch = new UserUpdateRequest("x", "x@x.com", "x", null);
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> userService.update(userId, patch, null))
            .isInstanceOf(UserNotFoundException.class);
    }

    // 삭제
    @Test
    @DisplayName("delete 성공(파일 삭제 포함)")
    void delete_success() {
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        userService.delete(userId);

        verify(userRepository).deleteById(userId);
        verify(fileStorage).deleteAvatarFile(username);
    }

    @Test
    @DisplayName("delete 실패: 사용자 없음")
    void delete_notFound() {
        given(userRepository.findById(userId)).willReturn(Optional.empty());
        assertThatThrownBy(() -> userService.delete(userId))
            .isInstanceOf(UserNotFoundException.class);
        verify(userRepository, never()).deleteById(anyLong());
        verify(fileStorage, never()).deleteAvatarFile(anyString());
    }


}