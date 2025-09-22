package com.codeit.data.dto.post;

import com.codeit.data.entity.Category;
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
public class PostCreateRequest {
    private String title;
    private String content;
    private String tags;
    private Category category;
    private Long authorId;

    public Post toPost(User author) {
        return Post.builder()
                .title(title)
                .content(content)
                .tags(tags)
                .category(category)
                .author(author)
                .build();
    }
}
