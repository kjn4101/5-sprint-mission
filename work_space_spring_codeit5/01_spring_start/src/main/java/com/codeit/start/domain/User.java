package com.codeit.start.domain;

import lombok.*;

// lombok 사용
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User {
    private Long id;
    private String username;
    private String password;
    private String name;
}
