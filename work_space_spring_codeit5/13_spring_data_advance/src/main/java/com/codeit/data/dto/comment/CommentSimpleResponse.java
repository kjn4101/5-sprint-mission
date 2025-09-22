package com.codeit.data.dto.comment;


import java.time.Instant;

public record CommentSimpleResponse(
    Long id,
    String content,
    String authorName,
    Instant createdAt
) {}
