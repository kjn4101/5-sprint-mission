package com.codeit.data.dto.post;

import com.codeit.data.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostUpdateRequest {
    private String title;
    private String content;
    private String tags;
    private Category category;
}
