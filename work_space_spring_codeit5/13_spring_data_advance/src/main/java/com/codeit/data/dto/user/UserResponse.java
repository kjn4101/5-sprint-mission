package com.codeit.data.dto.user;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String username;
    private String email;
    private String nickname;
}
