package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
@DisplayName("UserRepository 슬라이스 테스트")
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  private User testUser1;
  private User testUser2;

  @BeforeEach
  void setUp() {
    // 테스트 데이터 생성
    testUser1 = new User("testuser1", "test1@example.com", "password123", null);
    UserStatus status1 = new UserStatus(testUser1, Instant.now());
    
    testUser2 = new User("testuser2", "test2@example.com", "password456", null);
    UserStatus status2 = new UserStatus(testUser2, Instant.now());
    
    userRepository.saveAll(List.of(testUser1, testUser2));
  }

  @Test
  @DisplayName("username으로 사용자 조회 - 성공")
  void findByUsername_Success() {
    // when
    Optional<User> result = userRepository.findByUsername("testuser1");

    // then
    assertThat(result).isPresent();
    assertThat(result.get().getUsername()).isEqualTo("testuser1");
    assertThat(result.get().getEmail()).isEqualTo("test1@example.com");
  }

  @Test
  @DisplayName("username으로 사용자 조회 - 실패 (존재하지 않는 사용자)")
  void findByUsername_NotFound() {
    // when
    Optional<User> result = userRepository.findByUsername("nonexistent");

    // then
    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("email 존재 여부 확인 - 성공")
  void existsByEmail_True() {
    // when
    boolean exists = userRepository.existsByEmail("test1@example.com");

    // then
    assertThat(exists).isTrue();
  }

  @Test
  @DisplayName("email 존재 여부 확인 - 실패 (존재하지 않는 이메일)")
  void existsByEmail_False() {
    // when
    boolean exists = userRepository.existsByEmail("nonexistent@example.com");

    // then
    assertThat(exists).isFalse();
  }

  @Test
  @DisplayName("username 존재 여부 확인 - 성공")
  void existsByUsername_True() {
    // when
    boolean exists = userRepository.existsByUsername("testuser1");

    // then
    assertThat(exists).isTrue();
  }

  @Test
  @DisplayName("username 존재 여부 확인 - 실패 (존재하지 않는 사용자명)")
  void existsByUsername_False() {
    // when
    boolean exists = userRepository.existsByUsername("nonexistent");

    // then
    assertThat(exists).isFalse();
  }

  @Test
  @DisplayName("프로필과 상태 정보를 포함한 모든 사용자 조회 - 성공")
  void findAllWithProfileAndStatus_Success() {
    // when
    List<User> users = userRepository.findAllWithProfileAndStatus();

    // then
    assertThat(users).hasSize(2);
    assertThat(users).extracting(User::getUsername)
        .containsExactlyInAnyOrder("testuser1", "testuser2");
    
    // fetch join 확인
    users.forEach(user -> {
      assertThat(user.getStatus()).isNotNull();
    });
  }

  @Test
  @DisplayName("프로필과 상태 정보를 포함한 모든 사용자 조회 - 사용자 없음")
  void findAllWithProfileAndStatus_Empty() {
    // given
    userRepository.deleteAll();

    // when
    List<User> users = userRepository.findAllWithProfileAndStatus();

    // then
    assertThat(users).isEmpty();
  }
}