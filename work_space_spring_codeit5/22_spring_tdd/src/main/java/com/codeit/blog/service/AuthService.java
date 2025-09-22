package com.codeit.blog.service;


import com.codeit.blog.actuator.AuthMetrics;
import com.codeit.blog.dto.user.UserLoginRequest;
import com.codeit.blog.dto.user.UserResponse;
import com.codeit.blog.entity.User;
import com.codeit.blog.exception.user.InvalidCredentialsException;
import com.codeit.blog.exception.user.UserNotFoundException;
import com.codeit.blog.mapper.UserMapper;
import com.codeit.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthMetrics authMetrics;

    // login : 비지니스 로직에서 가장 핵심적인 로직 ★★★★★
    @Transactional(readOnly = true)
    public UserResponse login(UserLoginRequest loginRequest){
        log.debug("로그인 시도 : username={}", loginRequest.username());
        authMetrics.incrementLoginAttempt(); // 로그인 시도 카운트!

        User user = userRepository.findByUsername(loginRequest.username())
                .orElseThrow(() -> UserNotFoundException.withUsername(loginRequest.username()));

        if (!user.getPassword().equals(loginRequest.password())) {
            throw InvalidCredentialsException.wrongPassword();
        }

        log.info("로그인 성공 : username={}", loginRequest.username());
        authMetrics.onLoginSuccess(loginRequest.username()); // 성공 횟수 카운트!
        return userMapper.toUserDetailResponse(user);
    }
}
