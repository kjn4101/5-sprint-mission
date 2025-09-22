package com.codeit.rest.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostPageRequest {
    private int size = 10; // default 값, 보여줄 Item의 갯수
    private int page = 1; // default 값, page 값 (base-1 기준)
}
