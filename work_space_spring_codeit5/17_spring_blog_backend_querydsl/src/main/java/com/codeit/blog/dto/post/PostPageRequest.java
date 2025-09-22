package com.codeit.blog.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostPageRequest {
    private int size = 10; // default 값
    private int page = 1; // default 값
}
