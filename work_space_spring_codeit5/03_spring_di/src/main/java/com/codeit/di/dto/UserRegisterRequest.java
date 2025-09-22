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
public class UserRegisterRequest {
    private Long id;
    private String username;
    private String password;
    private String name;

    public User toUser() {
        return User.builder()
                .id(id)
                .username(username)
                .password(password)
                .name(name)
                .build();
    }
}
