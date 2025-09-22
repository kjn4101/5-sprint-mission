package com.codeit.blog.service;


import com.codeit.blog.dto.user.UserResponse;
import com.codeit.blog.dto.user.UserLoginRequest;
import com.codeit.blog.entity.User;
import com.codeit.blog.mapper.UserMapper;
import com.codeit.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public UserResponse login(UserLoginRequest loginRequest){
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (!user.getPassword().equals(loginRequest.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return userMapper.toUserDetailResponse(user);
    }
}
