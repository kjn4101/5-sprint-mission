package com.codeit.blog.dto.post;

import com.codeit.blog.dto.comment.CommentResponse;
import com.codeit.blog.dto.file.AttachFileResponse;
import com.codeit.blog.dto.user.UserSimpleResponse;
import com.codeit.blog.entity.Category;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PostSimpleResponse {
    private Long id;
    private String title;
    private Category category;
    private String tags;
    private Instant createdAt;
    private UserSimpleResponse author;
}
