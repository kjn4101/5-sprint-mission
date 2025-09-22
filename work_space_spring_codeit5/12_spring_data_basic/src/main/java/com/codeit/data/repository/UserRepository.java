package com.codeit.data.repository;

import com.codeit.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface UserRepository extends JpaRepository<User, Long> {
    // 쿼리 메서드 선언 영역
    // 참고 페이지 : https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html

    // 쿼리 메서드란? 리포지토리에 정해져 있는 규칙대로 메서드 이름을 만들면 자동으로 쿼리로 변환하는 기능
    // -> 가벼운 쿼리는 쿼리메서드 권장 ★★★★★

    // username(사용자 아이디) User 찾아오기(회원 가입 시 id 체크할때)
//    User findByUsername(String username); // User로 만들경우 없으면 null
    Optional<User> findByUsername(String test); // User로 만들경우 없으면 null
    Optional<User> findByUsernameAndId(String test, Long tt); // User로 만들경우 없으면 null

    // email로 찾기
    Optional<User> findByEmail(String email);

    // email로 like절로 찾기
    List<User> findByEmailContaining(String email);

    // 중복 체크용
    boolean existsUserByUsername(String username);

    // 회원 가입 기간 검색 검색하기
    Collection<User> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}




















