package com.sprint.mission.discodeit.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;

@ExtendWith(MockitoExtension.class)
class BasicMessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ChannelRepository channelRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MessageMapper messageMapper;

    @Mock
    private BinaryContentStorage binaryContentStorage;

    @Mock
    private BinaryContentRepository binaryContentRepository;

    @Mock
    private PageResponseMapper pageResponseMapper;

    @InjectMocks
    private BasicMessageService messageService;

    private Message message;
    private MessageDto messageDto;
    private MessageCreateRequest messageCreateRequest;
    private MessageUpdateRequest messageUpdateRequest;
    private Channel channel;
    private User author;
    private UUID messageId;
    private UUID channelId;
    private UUID authorId;

    @BeforeEach
    void setUp() {
        messageId = UUID.randomUUID();
        channelId = UUID.randomUUID();
        authorId = UUID.randomUUID();

        channel = new Channel(ChannelType.PUBLIC, "test-channel", "Test Channel");
        author = new User("testuser", "test@example.com", "password", null);
        message = new Message("Test message content", channel, author, List.of());

        messageDto = new MessageDto(
            messageId,
            Instant.now(),
            Instant.now(),
            "Test message content",
            channelId,
            null,
            List.of()
        );

        messageCreateRequest = new MessageCreateRequest("Test message content", channelId, authorId);
        messageUpdateRequest = new MessageUpdateRequest("Updated message content");
    }

    @Test
    @DisplayName("메시지 생성 - 성공")
    void create_Success() {
        // given
        given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
        given(userRepository.findById(authorId)).willReturn(Optional.of(author));
        given(messageRepository.save(any(Message.class))).willReturn(message);
        given(messageMapper.toDto(any(Message.class))).willReturn(messageDto);

        // when
        MessageDto result = messageService.create(messageCreateRequest, List.of());

        // then
        assertThat(result).isNotNull();
        assertThat(result.content()).isEqualTo("Test message content");

        then(channelRepository).should().findById(channelId);
        then(userRepository).should().findById(authorId);
        then(messageRepository).should().save(any(Message.class));
        then(messageMapper).should().toDto(any(Message.class));
    }

    @Test
    @DisplayName("메시지 생성 - 실패: 채널 없음")
    void create_Fail_ChannelNotFound() {
        // given
        given(channelRepository.findById(channelId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> messageService.create(messageCreateRequest, List.of()))
            .isInstanceOf(ChannelNotFoundException.class);

        then(channelRepository).should().findById(channelId);
        then(userRepository).should(times(0)).findById(authorId);
    }

    @Test
    @DisplayName("메시지 생성 - 실패: 작성자 없음")
    void create_Fail_AuthorNotFound() {
        // given
        given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
        given(userRepository.findById(authorId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> messageService.create(messageCreateRequest, List.of()))
            .isInstanceOf(UserNotFoundException.class);

        then(channelRepository).should().findById(channelId);
        then(userRepository).should().findById(authorId);
    }

    @Test
    @DisplayName("메시지 수정 - 성공")
    void update_Success() {
        // given
        given(messageRepository.findById(messageId)).willReturn(Optional.of(message));
        given(messageMapper.toDto(any(Message.class))).willReturn(messageDto);

        // when
        MessageDto result = messageService.update(messageId, messageUpdateRequest);

        // then
        assertThat(result).isNotNull();

        then(messageRepository).should().findById(messageId);
        then(messageMapper).should().toDto(any(Message.class));
    }

    @Test
    @DisplayName("메시지 수정 - 실패: 메시지 없음")
    void update_Fail_MessageNotFound() {
        // given
        given(messageRepository.findById(messageId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> messageService.update(messageId, messageUpdateRequest))
            .isInstanceOf(MessageNotFoundException.class);

        then(messageRepository).should().findById(messageId);
    }

    @Test
    @DisplayName("메시지 삭제 - 성공")
    void delete_Success() {
        // given
        given(messageRepository.existsById(messageId)).willReturn(true);

        // when
        messageService.delete(messageId);

        // then
        then(messageRepository).should().existsById(messageId);
        then(messageRepository).should().deleteById(messageId);
    }

    @Test
    @DisplayName("메시지 삭제 - 실패: 메시지 없음")
    void delete_Fail_MessageNotFound() {
        // given
        given(messageRepository.existsById(messageId)).willReturn(false);

        // when & then
        assertThatThrownBy(() -> messageService.delete(messageId))
            .isInstanceOf(MessageNotFoundException.class);

        then(messageRepository).should().existsById(messageId);
        then(messageRepository).should(times(0)).deleteById(messageId);
    }

    @Test
    @DisplayName("메시지 조회 - 성공")
    void find_Success() {
        // given
        given(messageRepository.findById(messageId)).willReturn(Optional.of(message));
        given(messageMapper.toDto(any(Message.class))).willReturn(messageDto);

        // when
        MessageDto result = messageService.find(messageId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.content()).isEqualTo("Test message content");

        then(messageRepository).should().findById(messageId);
        then(messageMapper).should().toDto(any(Message.class));
    }

    @Test
    @DisplayName("메시지 조회 - 실패: 메시지 없음")
    void find_Fail_MessageNotFound() {
        // given
        given(messageRepository.findById(messageId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> messageService.find(messageId))
            .isInstanceOf(MessageNotFoundException.class);

        then(messageRepository).should().findById(messageId);
    }
}