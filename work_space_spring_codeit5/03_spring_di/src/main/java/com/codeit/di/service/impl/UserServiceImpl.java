package com.codeit.di.service.impl;


import com.codeit.di.domain.User;
import com.codeit.di.dto.UserLoginRequest;
import com.codeit.di.dto.UserLoginResponse;
import com.codeit.di.dto.UserRegisterRequest;
import com.codeit.di.dto.UserRegisterResponse;
import com.codeit.di.repository.UserRepository;
import com.codeit.di.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("userService")
@RequiredArgsConstructor
@Primary
public class UserServiceImpl implements UserService {

    // 최근 생각하는 최선의 방법 = 이름으로 맞춘다.
//    private final UserRepository userRepositoryImpl;

    private static User loginUser;
    // 별칭을 선언하면 자동으로 맞춰준다.
    private final UserRepository userRepository;

    @Override
    public UserLoginResponse login(UserLoginRequest request) {
        User foundUser = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));

        if (!foundUser.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        loginUser = foundUser;
        return UserLoginResponse.builder().username(foundUser.getUsername())
                .name(foundUser.getName()).roles(foundUser.getRoles())
                .message("로그인에 성공하였습니다.").build();
    }

    @Override
    public void logout() {
        loginUser = null;
    }

    @Override
    public Optional<User> getLoginUser() {
        return Optional.ofNullable(loginUser);
    }

//    @Override
//    public User register(User user) {
//        return userRepository.save(user);
//    }

    @Override
    public UserRegisterResponse register(UserRegisterRequest request) {
        User user = userRepository.save(request.toUser());
        return UserRegisterResponse.builder().username(user.getUsername())
                .name(user.getName()).message("등록에 성공하였습니다.").build();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.delete(id);
    }
}
