package com.codeit.rest.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    private Long id;
    private String title;
    private String content;
    private List<String> tags;
    private Category category;
    private Long authorId;
    private Instant createdAt;
    private Instant updatedAt;

    // 이미지 관리 필드
    private String imagePath;     // 다운로드 경로
    private String imageRealName; // 사용자가 업로드한 원본 파일명
}
