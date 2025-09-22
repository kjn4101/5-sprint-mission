package com.codeit.rest.dto.post;

import com.codeit.rest.entity.Category;
import com.codeit.rest.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCreateRequest {
    private String title;
    private String content;
    private List<String> tags;
    private Category category;
    private Long authorId;

    public Post toPost() {
        return Post.builder()
                .title(title)
                .content(content)
                .tags(tags)
                .category(category)
                .authorId(authorId)
                .build();
    }
}
