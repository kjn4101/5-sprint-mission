package com.codeit.blog.service;


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

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public UserResponse login(UserLoginRequest loginRequest){
        User user = userRepository.findByUsername(loginRequest.username())
                .orElseThrow(() -> UserNotFoundException.withUsername(loginRequest.username()));

        if (!user.getPassword().equals(loginRequest.password())) {
            throw InvalidCredentialsException.wrongPassword();
        }

        return userMapper.toUserDetailResponse(user);
    }
}
