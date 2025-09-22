package com.codeit.blog.dto.user;

import lombok.ToString;

import java.time.LocalDate;

public record UserCreateRequest(
        String username,
        String password,
        String email,
        String nickname,
        LocalDate birthday
) {
    @Override
    public String toString() {
        return "UserCreateRequest{" +
                "username='" + username + '\'' +
                ", password='" + '\'' +
                ", email='" + email + '\'' +
                ", nickname='" + nickname + '\'' +
                ", birthday=" + birthday +
                '}';
    }
}
