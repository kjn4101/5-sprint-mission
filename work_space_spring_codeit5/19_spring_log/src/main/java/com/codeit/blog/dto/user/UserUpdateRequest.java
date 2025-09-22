package com.codeit.blog.dto.user;


import java.time.LocalDate;

public record UserUpdateRequest(
        String password,
        String email,
        String nickname,
        LocalDate birthday
) {
}
