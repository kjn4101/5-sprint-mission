package com.codeit.di.service.impl;


import com.codeit.di.domain.User;
import com.codeit.di.dto.UserLoginRequest;
import com.codeit.di.dto.UserLoginResponse;
import com.codeit.di.dto.UserRegisterRequest;
import com.codeit.di.dto.UserRegisterResponse;
import com.codeit.di.repository.UserRepository;
import com.codeit.di.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("adminService")
@RequiredArgsConstructor
public class AdminServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserRepository adminRepository;

    private static User loginUser;


    @Override
    public UserLoginResponse login(UserLoginRequest request) {
        User foundUser = adminRepository.findByUsername(request.getUsername())
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

    @Override
    public UserRegisterResponse register(UserRegisterRequest request) {
        throw new UnsupportedOperationException("관리자는 회원 가입이 불가능합니다.");
    }

    @Override
    public Optional<User> getUserById(Long id) {
        Optional<User> foundUser = userRepository.findById(id);
        if (foundUser.isEmpty()) {
            return adminRepository.findById(id);
        }
        return foundUser;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<User> adminUsers = adminRepository.findAll();
        users.addAll(adminUsers);
        return users;
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.delete(id);
        // 관리자는 탈퇴 불가!
    }
}
