package com.sprint.mission.discodeit;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

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

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;

@ExtendWith(MockitoExtension.class)
class BasicChannelServiceTest {

    @Mock
    private ChannelRepository channelRepository;

    @Mock
    private ReadStatusRepository readStatusRepository;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ChannelMapper channelMapper;

    @InjectMocks
    private BasicChannelService channelService;

    private Channel publicChannel;
    private Channel privateChannel;
    private ChannelDto publicChannelDto;
    private ChannelDto privateChannelDto;
    private PublicChannelCreateRequest publicChannelCreateRequest;
    private PrivateChannelCreateRequest privateChannelCreateRequest;
    private PublicChannelUpdateRequest publicChannelUpdateRequest;
    private UUID channelId;
    private UUID userId1;
    private UUID userId2;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        channelId = UUID.randomUUID();
        userId1 = UUID.randomUUID();
        userId2 = UUID.randomUUID();

        publicChannel = new Channel(ChannelType.PUBLIC, "test-channel", "Test Channel Description");
        privateChannel = new Channel(ChannelType.PRIVATE, null, null);

        user1 = new User("user1", "user1@example.com", "password", null);
        user2 = new User("user2", "user2@example.com", "password", null);

        publicChannelDto = new ChannelDto(
            channelId,
            ChannelType.PUBLIC,
            "test-channel",
            "Test Channel Description",
            List.of(),
            null
        );

        privateChannelDto = new ChannelDto(
            channelId,
            ChannelType.PRIVATE,
            null,
            null,
            List.of(),
            null
        );

        publicChannelCreateRequest = new PublicChannelCreateRequest("test-channel", "Test Channel Description");
        privateChannelCreateRequest = new PrivateChannelCreateRequest(List.of(userId1, userId2));
        publicChannelUpdateRequest = new PublicChannelUpdateRequest("updated-channel", "Updated Description");
    }

    @Test
    @DisplayName("공개 채널 생성 - 성공")
    void createPublicChannel_Success() {
        // given
        given(channelRepository.save(any(Channel.class))).willReturn(publicChannel);
        given(channelMapper.toDto(any(Channel.class))).willReturn(publicChannelDto);

        // when
        ChannelDto result = channelService.create(publicChannelCreateRequest);

        // then
        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("test-channel");
        assertThat(result.type()).isEqualTo(ChannelType.PUBLIC);

        then(channelRepository).should().save(any(Channel.class));
        then(channelMapper).should().toDto(any(Channel.class));
    }

    @Test
    @DisplayName("비공개 채널 생성 - 성공")
    void createPrivateChannel_Success() {
        // given
        given(channelRepository.save(any(Channel.class))).willReturn(privateChannel);
        given(userRepository.findAllById(List.of(userId1, userId2))).willReturn(List.of(user1, user2));
        given(channelMapper.toDto(any(Channel.class))).willReturn(privateChannelDto);

        // when
        ChannelDto result = channelService.create(privateChannelCreateRequest);

        // then
        assertThat(result).isNotNull();

        then(channelRepository).should().save(any(Channel.class));
        then(userRepository).should().findAllById(List.of(userId1, userId2));
        then(readStatusRepository).should().saveAll(anyList());
        then(channelMapper).should().toDto(any(Channel.class));
    }

    @Test
    @DisplayName("채널 수정 - 성공")
    void update_Success() {
        // given
        given(channelRepository.findById(channelId)).willReturn(Optional.of(publicChannel));
        given(channelMapper.toDto(any(Channel.class))).willReturn(publicChannelDto);

        // when
        ChannelDto result = channelService.update(channelId, publicChannelUpdateRequest);

        // then
        assertThat(result).isNotNull();

        then(channelRepository).should().findById(channelId);
        then(channelMapper).should().toDto(any(Channel.class));
    }

    @Test
    @DisplayName("채널 수정 - 실패: 채널 없음")
    void update_Fail_ChannelNotFound() {
        // given
        given(channelRepository.findById(channelId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> channelService.update(channelId, publicChannelUpdateRequest))
            .isInstanceOf(ChannelNotFoundException.class);

        then(channelRepository).should().findById(channelId);
    }

    @Test
    @DisplayName("채널 수정 - 실패: 비공개 채널")
    void update_Fail_PrivateChannel() {
        // given
        given(channelRepository.findById(channelId)).willReturn(Optional.of(privateChannel));

        // when & then
        assertThatThrownBy(() -> channelService.update(channelId, publicChannelUpdateRequest))
            .isInstanceOf(PrivateChannelUpdateException.class);

        then(channelRepository).should().findById(channelId);
    }

    @Test
    @DisplayName("채널 삭제 - 성공")
    void delete_Success() {
        // given
        given(channelRepository.existsById(channelId)).willReturn(true);

        // when
        channelService.delete(channelId);

        // then
        then(channelRepository).should().existsById(channelId);
        then(messageRepository).should().deleteAllByChannelId(channelId);
        then(readStatusRepository).should().deleteAllByChannelId(channelId);
        then(channelRepository).should().deleteById(channelId);
    }

    @Test
    @DisplayName("채널 삭제 - 실패: 채널 없음")
    void delete_Fail_ChannelNotFound() {
        // given
        given(channelRepository.existsById(channelId)).willReturn(false);

        // when & then
        assertThatThrownBy(() -> channelService.delete(channelId))
            .isInstanceOf(ChannelNotFoundException.class);

        then(channelRepository).should().existsById(channelId);
        then(messageRepository).should(times(0)).deleteAllByChannelId(channelId);
    }

    @Test
    @DisplayName("사용자별 채널 조회 - 성공")
    void findAllByUserId_Success() {
        // given
        UUID userId = UUID.randomUUID();
        given(readStatusRepository.findAllByUserId(userId)).willReturn(List.of());
        given(channelRepository.findAllByTypeOrIdIn(eq(ChannelType.PUBLIC), eq(List.of()))).willReturn(List.of(publicChannel));
        given(channelMapper.toDto(any(Channel.class))).willReturn(publicChannelDto);

        // when
        List<ChannelDto> result = channelService.findAllByUserId(userId);

        // then
        assertThat(result).hasSize(1);

        then(readStatusRepository).should().findAllByUserId(userId);
        then(channelRepository).should().findAllByTypeOrIdIn(eq(ChannelType.PUBLIC), eq(List.of()));
    }
}