package com.codeit.blog.dto.user;

import java.time.LocalDate;


public record UserCreateRequest(
        String username,
        String password,
        String email,
        String nickname,
        LocalDate birthday
) {
}
