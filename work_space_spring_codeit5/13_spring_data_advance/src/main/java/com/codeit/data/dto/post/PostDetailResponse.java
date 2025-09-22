package com.codeit.data.dto.post;

import com.codeit.data.dto.comment.CommentSimpleResponse;
import com.codeit.data.entity.Category;

import java.time.Instant;
import java.util.List;

public record PostDetailResponse(
    Long id,
    String title,
    String content,
    String tags,
    Category category,
    Instant createdAt,
    String authorName,
    List<CommentSimpleResponse> comments
){}
