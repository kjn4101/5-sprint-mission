package com.codeit.rest.service;

import com.codeit.rest.dto.user.UserCreateRequest;
import com.codeit.rest.dto.user.UserUpdateRequest;
import com.codeit.rest.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User create(UserCreateRequest newUser);
    User findById(Long id);
    User findByUsername(String username);
    List<User> findAll();
    User update(Long id, UserUpdateRequest updateUser); // 부분 수정
    User updateAll(Long id, UserCreateRequest newUser); // 전체 수정
    void delete(Long id);
    boolean existsById(Long id);
    long count();
}
