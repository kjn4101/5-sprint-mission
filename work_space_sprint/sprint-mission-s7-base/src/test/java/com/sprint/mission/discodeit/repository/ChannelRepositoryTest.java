package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
@DisplayName("ChannelRepository 슬라이스 테스트")
class ChannelRepositoryTest {

  @Autowired
  private ChannelRepository channelRepository;

  private Channel publicChannel1;
  private Channel publicChannel2;
  private Channel privateChannel1;
  private Channel privateChannel2;

  @BeforeEach
  void setUp() {
    // PUBLIC 채널 생성
    publicChannel1 = new Channel(ChannelType.PUBLIC, "Public Channel 1", "First public channel");
    publicChannel2 = new Channel(ChannelType.PUBLIC, "Public Channel 2", "Second public channel");
    
    // PRIVATE 채널 생성
    privateChannel1 = new Channel(ChannelType.PRIVATE, "Private Channel 1", "First private channel");
    privateChannel2 = new Channel(ChannelType.PRIVATE, "Private Channel 2", "Second private channel");
    
    channelRepository.saveAll(List.of(publicChannel1, publicChannel2, privateChannel1, privateChannel2));
  }

  @Test
  @DisplayName("PUBLIC 타입의 모든 채널 조회 - 성공")
  void findAllByTypeOrIdIn_PublicChannelsOnly() {
    // when
    List<Channel> channels = channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, List.of());

    // then
    assertThat(channels).hasSize(2);
    assertThat(channels).extracting(Channel::getType)
        .containsOnly(ChannelType.PUBLIC);
    assertThat(channels).extracting(Channel::getName)
        .containsExactlyInAnyOrder("Public Channel 1", "Public Channel 2");
  }

  @Test
  @DisplayName("특정 ID의 PRIVATE 채널 조회 - 성공")
  void findAllByTypeOrIdIn_PrivateChannelsByIds() {
    // given
    List<UUID> privateChannelIds = List.of(privateChannel1.getId());

    // when
    List<Channel> channels = channelRepository.findAllByTypeOrIdIn(ChannelType.PRIVATE, privateChannelIds);

    // then
    assertThat(channels).hasSize(2);
    assertThat(channels).extracting(Channel::getId)
        .contains(privateChannel1.getId(), privateChannel2.getId());
  }

  @Test
  @DisplayName("PUBLIC 채널과 특정 PRIVATE 채널 조회 - 성공")
  void findAllByTypeOrIdIn_PublicAndSpecificPrivate() {
    // given
    List<UUID> specificPrivateIds = List.of(privateChannel1.getId());

    // when
    List<Channel> channels = channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, specificPrivateIds);

    // then
    assertThat(channels).hasSize(3); // PUBLIC 2개 + PRIVATE 1개
    assertThat(channels).extracting(Channel::getName)
        .containsExactlyInAnyOrder("Public Channel 1", "Public Channel 2", "Private Channel 1");
  }

  @Test
  @DisplayName("존재하지 않는 ID로 조회 - PRIVATE 타입만 반환")
  void findAllByTypeOrIdIn_NonExistentIds() {
    // given
    List<UUID> nonExistentIds = List.of(UUID.randomUUID());

    // when
    List<Channel> channels = channelRepository.findAllByTypeOrIdIn(ChannelType.PRIVATE, nonExistentIds);

    // then
    assertThat(channels).hasSize(2); // PRIVATE 채널만 반환
    assertThat(channels).extracting(Channel::getType)
        .containsOnly(ChannelType.PRIVATE);
  }

  @Test
  @DisplayName("빈 ID 리스트로 PUBLIC 채널 조회 - 성공")
  void findAllByTypeOrIdIn_EmptyIdList() {
    // when
    List<Channel> channels = channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, List.of());

    // then
    assertThat(channels).hasSize(2);
    assertThat(channels).allMatch(channel -> channel.getType() == ChannelType.PUBLIC);
  }
}