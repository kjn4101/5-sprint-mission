package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
@DisplayName("MessageRepository 슬라이스 테스트")
class MessageRepositoryTest {

  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private ChannelRepository channelRepository;

  @Autowired
  private UserRepository userRepository;

  private Channel testChannel;
  private User testUser;
  private Message message1;
  private Message message2;
  private Message message3;

  @BeforeEach
  void setUp() throws InterruptedException {
    // 테스트 사용자 생성
    testUser = new User("testuser", "test@example.com", "password", null);
    UserStatus status = new UserStatus(testUser, Instant.now());
    userRepository.save(testUser);

    // 테스트 채널 생성
    testChannel = new Channel(ChannelType.PUBLIC, "Test Channel", "Test Description");
    channelRepository.save(testChannel);

    // 메시지 생성 (시간 간격을 두고 생성)
    message1 = new Message("First message", testChannel, testUser, List.of());
    messageRepository.save(message1);
    
    Thread.sleep(10); // createdAt 시간 차이를 위한 대기
    
    message2 = new Message("Second message", testChannel, testUser, List.of());
    messageRepository.save(message2);
    
    Thread.sleep(10);
    
    message3 = new Message("Third message", testChannel, testUser, List.of());
    messageRepository.save(message3);
  }

  @Test
  @DisplayName("채널 ID와 작성자 정보를 포함한 메시지 조회 - 성공")
  void findAllByChannelIdWithAuthor_Success() {
    // given
    Instant futureTime = Instant.now().plus(1, ChronoUnit.HOURS);
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

    // when
    Slice<Message> messages = messageRepository.findAllByChannelIdWithAuthor(
        testChannel.getId(), futureTime, pageRequest);

    // then
    assertThat(messages.getContent()).hasSize(3);
    assertThat(messages.getContent()).extracting(Message::getContent)
        .containsExactly("Third message", "Second message", "First message");
    
    // fetch join 확인
    messages.getContent().forEach(message -> {
      assertThat(message.getAuthor()).isNotNull();
      assertThat(message.getAuthor().getStatus()).isNotNull();
    });
  }

  @Test
  @DisplayName("특정 시간 이전의 메시지만 조회 - 성공")
  void findAllByChannelIdWithAuthor_BeforeSpecificTime() {
    // given
    Instant cutoffTime = message2.getCreatedAt();
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

    // when
    Slice<Message> messages = messageRepository.findAllByChannelIdWithAuthor(
        testChannel.getId(), cutoffTime, pageRequest);

    // then
    assertThat(messages.getContent()).hasSize(1);
    assertThat(messages.getContent().get(0).getContent()).isEqualTo("First message");
  }

  @Test
  @DisplayName("페이징 처리 확인 - 성공")
  void findAllByChannelIdWithAuthor_Paging() {
    // given
    Instant futureTime = Instant.now().plus(1, ChronoUnit.HOURS);
    PageRequest pageRequest = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdAt"));

    // when
    Slice<Message> messages = messageRepository.findAllByChannelIdWithAuthor(
        testChannel.getId(), futureTime, pageRequest);

    // then
    assertThat(messages.getContent()).hasSize(2);
    assertThat(messages.hasNext()).isTrue();
    assertThat(messages.getContent()).extracting(Message::getContent)
        .containsExactly("Third message", "Second message");
  }

  @Test
  @DisplayName("채널의 마지막 메시지 시간 조회 - 성공")
  void findLastMessageAtByChannelId_Success() {
    // when
    Optional<Instant> lastMessageAt = messageRepository.findLastMessageAtByChannelId(testChannel.getId());

    // then
    assertThat(lastMessageAt.get().truncatedTo(ChronoUnit.MILLIS))
        .isEqualTo(message3.getCreatedAt().truncatedTo(ChronoUnit.MILLIS));
  }

  @Test
  @DisplayName("채널의 마지막 메시지 시간 조회 - 메시지 없음")
  void findLastMessageAtByChannelId_NoMessages() {
    // given
    Channel emptyChannel = new Channel(ChannelType.PUBLIC, "Empty Channel", "No messages");
    channelRepository.save(emptyChannel);

    // when
    Optional<Instant> lastMessageAt = messageRepository.findLastMessageAtByChannelId(emptyChannel.getId());

    // then
    assertThat(lastMessageAt).isEmpty();
  }

  @Test
  @DisplayName("채널의 모든 메시지 삭제 - 성공")
  void deleteAllByChannelId_Success() {
    // given
    long initialCount = messageRepository.count();
    assertThat(initialCount).isEqualTo(3);

    // when
    messageRepository.deleteAllByChannelId(testChannel.getId());
    messageRepository.flush();

    // then
    long finalCount = messageRepository.count();
    assertThat(finalCount).isZero();
  }

  @Test
  @DisplayName("존재하지 않는 채널의 메시지 삭제 - 아무 일도 일어나지 않음")
  void deleteAllByChannelId_NonExistentChannel() {
    // given
    long initialCount = messageRepository.count();

    // when
    messageRepository.deleteAllByChannelId(java.util.UUID.randomUUID());
    messageRepository.flush();

    // then
    long finalCount = messageRepository.count();
    assertThat(finalCount).isEqualTo(initialCount);
  }
}