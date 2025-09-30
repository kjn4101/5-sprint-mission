package com.sprint.mission.discodeit.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.basic.BasicBinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;

@ExtendWith(MockitoExtension.class)
class BasicBinaryContentServiceTest {

    @Mock
    private BinaryContentRepository binaryContentRepository;

    @Mock
    private BinaryContentMapper binaryContentMapper;

    @Mock
    private BinaryContentStorage binaryContentStorage;

    @InjectMocks
    private BasicBinaryContentService basicBinaryContentService;

    private BinaryContent binaryContent;
    private BinaryContentDto binaryContentDto;
    private BinaryContentCreateRequest createRequest;
    private UUID binaryContentId;

    @BeforeEach
    void setUp() {
        binaryContentId = UUID.randomUUID();

        byte[] testBytes = "test file content".getBytes();
        createRequest = new BinaryContentCreateRequest("test.txt", "text/plain", testBytes);

        binaryContent = new BinaryContent("test.txt", (long) testBytes.length, "text/plain");
        binaryContentDto = new BinaryContentDto(binaryContentId, "test.txt", 17L, "text/plain");
    }

    @Test
    @DisplayName("바이너리 콘텐츠 생성 - 성공")
    void create_Success() {
        // given
        BinaryContent savedBinaryContent = new BinaryContent("test.txt", 17L, "text/plain");
        given(binaryContentRepository.save(any(BinaryContent.class))).willReturn(binaryContent);
        given(binaryContentMapper.toDto(any(BinaryContent.class))).willReturn(binaryContentDto);

        // when
        BinaryContentDto result = basicBinaryContentService.create(createRequest);

        // then
        assertThat(result).isNotNull();
        assertThat(result.fileName()).isEqualTo("test.txt");
        assertThat(result.size()).isEqualTo(17L);

        then(binaryContentRepository).should().save(any(BinaryContent.class));
        // ID가 null이므로 정확한 검증 대신 호출 여부만 확인
        then(binaryContentStorage).should().put(any(), any(byte[].class));
        then(binaryContentMapper).should().toDto(any(BinaryContent.class));
    }

    @Test
    @DisplayName("바이너리 콘텐츠 단일 조회 - 성공")
    void find_Success() {
        // given
        given(binaryContentRepository.findById(binaryContentId)).willReturn(Optional.of(binaryContent));
        given(binaryContentMapper.toDto(binaryContent)).willReturn(binaryContentDto);

        // when
        BinaryContentDto result = basicBinaryContentService.find(binaryContentId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.fileName()).isEqualTo("test.txt");

        then(binaryContentRepository).should().findById(binaryContentId);
        then(binaryContentMapper).should().toDto(binaryContent);
    }

    @Test
    @DisplayName("바이너리 콘텐츠 단일 조회 - 실패: 존재하지 않음")
    void find_Fail_NotFound() {
        // given
        given(binaryContentRepository.findById(binaryContentId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> basicBinaryContentService.find(binaryContentId))
            .isInstanceOf(BinaryContentNotFoundException.class);

        then(binaryContentRepository).should().findById(binaryContentId);
        then(binaryContentMapper).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("바이너리 콘텐츠 삭제 - 성공")
    void delete_Success() {
        // given
        given(binaryContentRepository.existsById(binaryContentId)).willReturn(true);

        // when
        basicBinaryContentService.delete(binaryContentId);

        // then
        then(binaryContentRepository).should().existsById(binaryContentId);
        then(binaryContentRepository).should().deleteById(binaryContentId);
    }

    @Test
    @DisplayName("바이너리 콘텐츠 삭제 - 실패: 존재하지 않음")
    void delete_Fail_NotFound() {
        // given
        given(binaryContentRepository.existsById(binaryContentId)).willReturn(false);

        // when & then
        assertThatThrownBy(() -> basicBinaryContentService.delete(binaryContentId))
            .isInstanceOf(BinaryContentNotFoundException.class);

        then(binaryContentRepository).should().existsById(binaryContentId);
        then(binaryContentRepository).should(times(0)).deleteById(binaryContentId);
    }
}