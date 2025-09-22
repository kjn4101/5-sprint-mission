package com.codeit.blog.dto.comment;

import com.codeit.blog.dto.post.PostOnlyIdResponse;
import com.codeit.blog.dto.user.UserSimpleResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {
    private Long id;
    private String content;
    private Instant createdAt;

    private UserSimpleResponse author;
    private PostOnlyIdResponse post;
}
