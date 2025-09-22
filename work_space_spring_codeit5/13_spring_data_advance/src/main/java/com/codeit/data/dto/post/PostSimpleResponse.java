package com.codeit.data.dto.post;

import com.codeit.data.entity.Category;

import java.time.Instant;

// 전체가 아닌 list 목록에 보일때 사용하는 응답값
public record PostSimpleResponse(
    Long id,
    String title,
    String tags,
    Category category,
    Instant createdAt,
    String authorName
) {}
