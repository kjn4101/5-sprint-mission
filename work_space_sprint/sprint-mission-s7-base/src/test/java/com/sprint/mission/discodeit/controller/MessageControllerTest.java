package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.service.MessageService;

@WebMvcTest(MessageController.class)
@DisplayName("MessageController 슬라이스 테스트")
class MessageControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private MessageService messageService;

  @Test
  @DisplayName("메시지 생성 - 성공")
  void createMessage_Success() throws Exception {
    // given
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();

    MessageCreateRequest request = new MessageCreateRequest(
        "Hello, World!",
        channelId,
        authorId
    );

    UUID messageId = UUID.randomUUID();
    MessageDto response = new MessageDto(
        messageId,
        Instant.now(),
        Instant.now(),
        "Hello, World!",
        channelId,
        null,
        List.of()
    );

    String requestJson = objectMapper.writeValueAsString(request);
    MockMultipartFile messagePart = new MockMultipartFile(
        "messageCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        requestJson.getBytes()
    );

    given(messageService.create(eq(request), any())).willReturn(response);

    // when & then
    mockMvc.perform(multipart("/api/messages")
            .file(messagePart)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(messageId.toString()))
        .andExpect(jsonPath("$.content").value("Hello, World!"));
  }

  @Test
  @DisplayName("메시지 생성 - 유효성 검증 실패 (content 누락)")
  void createMessage_ValidationFailed() throws Exception {
    // given
    MessageCreateRequest request = new MessageCreateRequest(
        "",  // 빈 내용
        UUID.randomUUID(),
        UUID.randomUUID()
    );

    String requestJson = objectMapper.writeValueAsString(request);
    MockMultipartFile messagePart = new MockMultipartFile(
        "messageCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        requestJson.getBytes()
    );

    // when & then
    mockMvc.perform(multipart("/api/messages")
            .file(messagePart)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("VALIDATION_FAILED"));
  }

  @Test
  @DisplayName("메시지 수정 - 성공")
  void updateMessage_Success() throws Exception {
    // given
    UUID messageId = UUID.randomUUID();
    MessageUpdateRequest request = new MessageUpdateRequest("Updated content");

    MessageDto response = new MessageDto(
        messageId,
        Instant.now(),
        Instant.now(),
        "Updated content",
        UUID.randomUUID(),
        null,
        List.of()
    );

    given(messageService.update(eq(messageId), eq(request))).willReturn(response);

    // when & then
    mockMvc.perform(patch("/api/messages/{messageId}", messageId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").value("Updated content"));
  }

  @Test
  @DisplayName("메시지 수정 - 메시지 없음")
  void updateMessage_MessageNotFound() throws Exception {
    // given
    UUID messageId = UUID.randomUUID();
    MessageUpdateRequest request = new MessageUpdateRequest("Updated content");

    given(messageService.update(eq(messageId), any()))
        .willThrow(new MessageNotFoundException(messageId));

    // when & then
    mockMvc.perform(patch("/api/messages/{messageId}", messageId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.exceptionType").value("MessageNotFoundException"));
  }

  @Test
  @DisplayName("메시지 삭제 - 성공")
  void deleteMessage_Success() throws Exception {
    // given
    UUID messageId = UUID.randomUUID();
    doNothing().when(messageService).delete(messageId);

    // when & then
    mockMvc.perform(delete("/api/messages/{messageId}", messageId))
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("메시지 삭제 - 메시지 없음")
  void deleteMessage_MessageNotFound() throws Exception {
    // given
    UUID messageId = UUID.randomUUID();
    doThrow(new MessageNotFoundException(messageId))
        .when(messageService).delete(messageId);

    // when & then
    mockMvc.perform(delete("/api/messages/{messageId}", messageId))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.exceptionType").value("MessageNotFoundException"));
  }

  @Test
  @DisplayName("채널의 메시지 목록 조회 - 성공")
  void findAllMessagesByChannelId_Success() throws Exception {
    // given
    UUID channelId = UUID.randomUUID();
    Instant cursor = Instant.now();

    List<MessageDto> messages = List.of(
        new MessageDto(UUID.randomUUID(), Instant.now(), Instant.now(), "Message 1", UUID.randomUUID(), null, List.of()),
        new MessageDto(UUID.randomUUID(), Instant.now(), Instant.now(), "Message 2", UUID.randomUUID(), null, List.of())
    );

    PageResponse<MessageDto> response = new PageResponse<>(
        messages,
        Instant.now(),
        50,
        true,
        100L
    );

    given(messageService.findAllByChannelId(eq(channelId), eq(cursor), any()))
        .willReturn(response);

    // when & then
    mockMvc.perform(get("/api/messages")
            .param("channelId", channelId.toString())
            .param("cursor", cursor.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(2))
        .andExpect(jsonPath("$.hasNext").value(true))
        .andExpect(jsonPath("$.size").value(50));
  }

  @Test
  @DisplayName("채널의 메시지 목록 조회 - cursor 없이 조회")
  void findAllMessagesByChannelId_WithoutCursor() throws Exception {
    // given
    UUID channelId = UUID.randomUUID();

    List<MessageDto> messages = List.of(
        new MessageDto(UUID.randomUUID(), Instant.now(), Instant.now(), "Message 1", UUID.randomUUID(), null, List.of())
    );

    PageResponse<MessageDto> response = new PageResponse<>(
        messages,
        null,
        50,
        false,
        1L
    );

    given(messageService.findAllByChannelId(eq(channelId), eq(null), any()))
        .willReturn(response);

    // when & then
    mockMvc.perform(get("/api/messages")
            .param("channelId", channelId.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(1))
        .andExpect(jsonPath("$.hasNext").value(false));
  }
}