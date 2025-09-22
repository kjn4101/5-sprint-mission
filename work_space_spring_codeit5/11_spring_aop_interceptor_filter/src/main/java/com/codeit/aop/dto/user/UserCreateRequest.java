package com.codeit.aop.dto.user;

import com.codeit.aop.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateRequest {
    private String username;
    private String password;
    private String email;
    private String nickname;
    private LocalDate birthday;

    public User toUser(){
        return User.builder()
                .username(username)
                .password(password)
                .email(email)
                .nickname(nickname)
                .birthday(birthday)
                .build();
    }
}
