package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
@DisplayName("ReadStatusRepository 슬라이스 테스트")
class ReadStatusRepositoryTest {

  @Autowired
  private ReadStatusRepository readStatusRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ChannelRepository channelRepository;

  private User testUser1;
  private User testUser2;
  private Channel testChannel1;
  private Channel testChannel2;

  @BeforeEach
  void setUp() {
    // 사용자 생성
    testUser1 = new User("user1", "user1@example.com", "password", null);
    UserStatus status1 = new UserStatus(testUser1, Instant.now());

    testUser2 = new User("user2", "user2@example.com", "password", null);
    UserStatus status2 = new UserStatus(testUser2, Instant.now());

    userRepository.saveAll(List.of(testUser1, testUser2));

    // 채널 생성
    testChannel1 = new Channel(ChannelType.PUBLIC, "Channel 1", "Description 1");
    testChannel2 = new Channel(ChannelType.PUBLIC, "Channel 2", "Description 2");
    channelRepository.saveAll(List.of(testChannel1, testChannel2));

    // 읽음 상태 생성
    ReadStatus readStatus1 = new ReadStatus(testUser1, testChannel1, Instant.now());
    ReadStatus readStatus2 = new ReadStatus(testUser1, testChannel2, Instant.now());
    ReadStatus readStatus3 = new ReadStatus(testUser2, testChannel1, Instant.now());

    readStatusRepository.saveAll(List.of(readStatus1, readStatus2, readStatus3));
  }

  @Test
  @DisplayName("사용자 ID로 읽음 상태 조회 - 성공")
  void findAllByUserId_Success() {
    // when
    List<ReadStatus> readStatuses = readStatusRepository.findAllByUserId(testUser1.getId());

    // then
    assertThat(readStatuses).hasSize(2);
    assertThat(readStatuses).allMatch(rs -> rs.getUser().getId().equals(testUser1.getId()));
  }

  @Test
  @DisplayName("사용자 ID로 읽음 상태 조회 - 읽음 상태 없음")
  void findAllByUserId_Empty() {
    // given
    User newUser = new User("newuser", "new@example.com", "password", null);
    UserStatus newStatus = new UserStatus(newUser, Instant.now());
    userRepository.save(newUser);

    // when
    List<ReadStatus> readStatuses = readStatusRepository.findAllByUserId(newUser.getId());

    // then
    assertThat(readStatuses).isEmpty();
  }

  @Test
  @DisplayName("채널 ID와 사용자 정보를 포함한 읽음 상태 조회 - 성공")
  void findAllByChannelIdWithUser_Success() {
    // when
    List<ReadStatus> readStatuses = readStatusRepository.findAllByChannelIdWithUser(testChannel1.getId());

    // then
    assertThat(readStatuses).hasSize(2);
    assertThat(readStatuses).allMatch(rs -> rs.getChannel().getId().equals(testChannel1.getId()));

    // fetch join 확인
    readStatuses.forEach(rs -> {
      assertThat(rs.getUser()).isNotNull();
      assertThat(rs.getUser().getStatus()).isNotNull();
    });
  }

  @Test
  @DisplayName("채널 ID와 사용자 정보를 포함한 읽음 상태 조회 - 읽음 상태 없음")
  void findAllByChannelIdWithUser_Empty() {
    // given
    Channel newChannel = new Channel(ChannelType.PUBLIC, "New Channel", "New Description");
    channelRepository.save(newChannel);

    // when
    List<ReadStatus> readStatuses = readStatusRepository.findAllByChannelIdWithUser(newChannel.getId());

    // then
    assertThat(readStatuses).isEmpty();
  }

  @Test
  @DisplayName("사용자와 채널로 읽음 상태 존재 확인 - 존재함")
  void existsByUserIdAndChannelId_True() {
    // when
    Boolean exists = readStatusRepository.existsByUserIdAndChannelId(
        testUser1.getId(), testChannel1.getId());

    // then
    assertThat(exists).isTrue();
  }

  @Test
  @DisplayName("사용자와 채널로 읽음 상태 존재 확인 - 존재하지 않음")
  void existsByUserIdAndChannelId_False() {
    // when
    Boolean exists = readStatusRepository.existsByUserIdAndChannelId(
        testUser2.getId(), testChannel2.getId());

    // then
    assertThat(exists).isFalse();
  }

  @Test
  @DisplayName("채널의 모든 읽음 상태 삭제 - 성공")
  void deleteAllByChannelId_Success() {
    // given
    long initialCount = readStatusRepository.count();
    assertThat(initialCount).isEqualTo(3);

    // when
    readStatusRepository.deleteAllByChannelId(testChannel1.getId());
    readStatusRepository.flush();

    // then
    long finalCount = readStatusRepository.count();
    assertThat(finalCount).isEqualTo(1); // testUser1-testChannel2만 남음
  }

  @Test
  @DisplayName("존재하지 않는 채널의 읽음 상태 삭제 - 아무 일도 일어나지 않음")
  void deleteAllByChannelId_NonExistentChannel() {
    // given
    long initialCount = readStatusRepository.count();

    // when
    readStatusRepository.deleteAllByChannelId(java.util.UUID.randomUUID());
    readStatusRepository.flush();

    // then
    long finalCount = readStatusRepository.count();
    assertThat(finalCount).isEqualTo(initialCount);
  }
}