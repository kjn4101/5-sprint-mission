package com.codeit.di.repository;

import com.codeit.di.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    List<User> findAll();

    long count();

    void delete(Long id);

    boolean existsById(Long id);
}
