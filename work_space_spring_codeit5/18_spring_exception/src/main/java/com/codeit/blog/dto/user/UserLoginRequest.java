package com.codeit.blog.dto.user;

public record UserLoginRequest(
        String username,
        String password
) {
}
