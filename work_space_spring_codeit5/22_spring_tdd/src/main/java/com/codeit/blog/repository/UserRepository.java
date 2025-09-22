package com.codeit.blog.repository;

import com.codeit.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>  {
    // username 으로 사용자 찾기
    Optional<User> findByUsername(String username);

    // email 로 사용자 찾기
    Optional<User> findByEmail(String email);

    // username 중복 체크
    boolean existsByUsername(String username);

    // email 중복 체크
    boolean existsByEmail(String email);

}
