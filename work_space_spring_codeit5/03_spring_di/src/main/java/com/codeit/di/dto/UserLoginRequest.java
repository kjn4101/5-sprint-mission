package com.codeit.di.dto;

import com.codeit.di.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserLoginRequest {
    private String username;
    private String password;
    private String roles;

    // User로 변환하는 방법
    public User toUser() {
        return User.builder()
                .username(username)
                .password(password)
                .roles(roles)
                .build();
    }
}
