package com.codeit.blog.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostFlatDto {
    private Long id;
    private String title;
    private Instant createdAt;
    private Long commentCount;
    private String authorUsername;
    private String authorNickname;
}
