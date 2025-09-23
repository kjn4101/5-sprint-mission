package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.service.ChannelService;

@WebMvcTest(ChannelController.class)
@DisplayName("ChannelController 슬라이스 테스트")
class ChannelControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private ChannelService channelService;

  @Test
  @DisplayName("공개 채널 생성 - 성공")
  void createPublicChannel_Success() throws Exception {
    // given
    PublicChannelCreateRequest request = new PublicChannelCreateRequest(
        "General Chat",
        "General discussion channel"
    );

    UUID channelId = UUID.randomUUID();
    ChannelDto response = new ChannelDto(
        channelId,
        ChannelType.PUBLIC,
        "General Chat",
        "General discussion channel",
        null,
        null
    );

    given(channelService.create(request)).willReturn(response);

    // when & then
    mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(channelId.toString()))
        .andExpect(jsonPath("$.type").value("PUBLIC"))
        .andExpect(jsonPath("$.name").value("General Chat"));
  }

  @Test
  @DisplayName("공개 채널 생성 - 유효성 검증 실패 (name 누락)")
  void createPublicChannel_ValidationFailed() throws Exception {
    // given
    PublicChannelCreateRequest request = new PublicChannelCreateRequest(
        "",  // 빈 채널명
        "Description"
    );

    // when & then
    mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("VALIDATION_FAILED"));
  }

  @Test
  @DisplayName("비공개 채널 생성 - 성공")
  void createPrivateChannel_Success() throws Exception {
    // given
    PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(
        List.of(UUID.randomUUID(), UUID.randomUUID())
    );

    UUID channelId = UUID.randomUUID();
    ChannelDto response = new ChannelDto(
        channelId,
        ChannelType.PRIVATE,
        null,
        null,
        null,
        null
    );

    given(channelService.create(request)).willReturn(response);

    // when & then
    mockMvc.perform(post("/api/channels/private")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(channelId.toString()))
        .andExpect(jsonPath("$.type").value("PRIVATE"));
  }

  @Test
  @DisplayName("비공개 채널 생성 - 유효성 검증 실패 (참가자 부족)")
  void createPrivateChannel_ValidationFailed() throws Exception {
    // given
    PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(
        List.of(UUID.randomUUID())  // 1명만 (최소 2명 필요)
    );

    // when & then
    mockMvc.perform(post("/api/channels/private")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("VALIDATION_FAILED"));
  }

  @Test
  @DisplayName("채널 수정 - 성공")
  void updateChannel_Success() throws Exception {
    // given
    UUID channelId = UUID.randomUUID();
    PublicChannelUpdateRequest request = new PublicChannelUpdateRequest(
        "Updated Channel",
        "Updated description"
    );

    ChannelDto response = new ChannelDto(
        channelId,
        ChannelType.PUBLIC,
        "Updated Channel",
        "Updated description",
        null,
        null
    );

    given(channelService.update(eq(channelId), eq(request))).willReturn(response);

    // when & then
    mockMvc.perform(patch("/api/channels/{channelId}", channelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Updated Channel"))
        .andExpect(jsonPath("$.description").value("Updated description"));
  }

  @Test
  @DisplayName("채널 수정 - 채널 없음")
  void updateChannel_ChannelNotFound() throws Exception {
    // given
    UUID channelId = UUID.randomUUID();
    PublicChannelUpdateRequest request = new PublicChannelUpdateRequest(
        "Updated Channel",
        "Updated description"
    );

    given(channelService.update(eq(channelId), any()))
        .willThrow(new ChannelNotFoundException(channelId));

    // when & then
    mockMvc.perform(patch("/api/channels/{channelId}", channelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.exceptionType").value("ChannelNotFoundException"));
  }

  @Test
  @DisplayName("채널 삭제 - 성공")
  void deleteChannel_Success() throws Exception {
    // given
    UUID channelId = UUID.randomUUID();
    doNothing().when(channelService).delete(channelId);

    // when & then
    mockMvc.perform(delete("/api/channels/{channelId}", channelId))
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("채널 삭제 - 채널 없음")
  void deleteChannel_ChannelNotFound() throws Exception {
    // given
    UUID channelId = UUID.randomUUID();
    doThrow(new ChannelNotFoundException(channelId))
        .when(channelService).delete(channelId);

    // when & then
    mockMvc.perform(delete("/api/channels/{channelId}", channelId))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.exceptionType").value("ChannelNotFoundException"));
  }

  @Test
  @DisplayName("사용자의 채널 목록 조회 - 성공")
  void findAllChannelsByUserId_Success() throws Exception {
    // given
    UUID userId = UUID.randomUUID();
    List<ChannelDto> channels = List.of(
        new ChannelDto(UUID.randomUUID(), ChannelType.PUBLIC, "Channel 1", "Desc 1", null, null),
        new ChannelDto(UUID.randomUUID(), ChannelType.PRIVATE, null, null, null, null)
    );

    given(channelService.findAllByUserId(userId)).willReturn(channels);

    // when & then
    mockMvc.perform(get("/api/channels")
            .param("userId", userId.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].type").value("PUBLIC"))
        .andExpect(jsonPath("$[1].type").value("PRIVATE"));
  }
}