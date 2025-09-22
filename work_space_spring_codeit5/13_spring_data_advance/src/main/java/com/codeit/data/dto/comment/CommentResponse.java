package com.codeit.data.dto.comment;


import com.codeit.data.dto.post.PostResponseForComment;
import com.codeit.data.dto.user.UserResponse;
import com.codeit.data.entity.Post;
import com.codeit.data.entity.User;
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

    private UserResponse author;
    private PostResponseForComment post;
}
