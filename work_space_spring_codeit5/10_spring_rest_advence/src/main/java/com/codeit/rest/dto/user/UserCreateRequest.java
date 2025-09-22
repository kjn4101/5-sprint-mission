package com.codeit.rest.dto.user;

import com.codeit.rest.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

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
