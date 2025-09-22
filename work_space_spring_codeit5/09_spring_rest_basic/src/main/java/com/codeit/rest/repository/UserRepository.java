package com.codeit.rest.repository;

import com.codeit.rest.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    List<User> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
    long count();
}