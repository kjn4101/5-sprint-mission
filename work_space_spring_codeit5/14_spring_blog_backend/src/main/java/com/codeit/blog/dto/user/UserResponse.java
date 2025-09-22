package com.codeit.blog.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String username;
//    private String password;
    private String email;
    private String nickname;
    private boolean hasAvatar;
    private String avatarUrl;
    private Instant createdAt;
    private Instant updatedAt;
}
