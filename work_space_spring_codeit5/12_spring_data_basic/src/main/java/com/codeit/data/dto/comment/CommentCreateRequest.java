package com.codeit.data.dto.comment;

import com.codeit.data.entity.Comment;
import com.codeit.data.entity.Post;
import com.codeit.data.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentCreateRequest {
    private String content;
    private Long authorId;
    private Long postId;

    // 엔티티 변환
    public Comment toComment(User author, Post post) {
        return Comment.builder()
                .content(content)
                .author(author)
                .post(post)
                .build();
    }
}
