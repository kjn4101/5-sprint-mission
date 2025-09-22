package com.codeit.data.dto.user;

import com.codeit.data.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequest {
    private String password;
    private String email;
    private String nickname;

    public User toUser() {
        return User.builder()
                .password(password)
                .email(email)
                .nickname(nickname)
                .build();
    }
}
