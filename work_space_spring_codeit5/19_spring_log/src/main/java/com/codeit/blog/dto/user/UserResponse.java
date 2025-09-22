package com.codeit.blog.dto.user;

import java.time.Instant;
import java.time.LocalDate;

public record UserResponse(
        Long id,
        String username,
        String password,
        String email,
        String nickname,
        LocalDate birthday,
        Instant createdAt,
        Instant updatedAt
) {
}
