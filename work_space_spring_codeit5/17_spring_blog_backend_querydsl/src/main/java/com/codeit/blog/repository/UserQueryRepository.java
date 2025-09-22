package com.codeit.blog.repository;

import com.codeit.blog.entity.User;

import java.util.List;
import java.util.Optional;


// QueryDSL에서 사용 할 인터페이스
public interface UserQueryRepository {
    Optional<User> findByIdQ(Long id);
    List<User> findAllOrderByCreatedDesc();
    Optional<User> findByLoginId(String usernameOrEmail);
}
