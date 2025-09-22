package com.codeit.di.service;

import com.codeit.di.domain.User;
import com.codeit.di.dto.UserLoginRequest;
import com.codeit.di.dto.UserLoginResponse;
import com.codeit.di.dto.UserRegisterRequest;
import com.codeit.di.dto.UserRegisterResponse;

import java.util.List;
import java.util.Optional;

public interface UserService {
//    User login(String username, String password);
    UserLoginResponse login(UserLoginRequest request);
    void logout();
    Optional<User> getLoginUser();

    UserRegisterResponse register(UserRegisterRequest request);
    Optional<User> getUserById(Long id);
    List<User> getAllUsers();
    void deleteUser(Long id);
}

