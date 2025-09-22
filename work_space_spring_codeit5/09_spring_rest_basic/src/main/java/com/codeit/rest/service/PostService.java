package com.codeit.rest.service;

//import com.codeit.rest.dto.common.SimplePageResponse;
import com.codeit.rest.dto.common.SimplePageResponse;
import com.codeit.rest.dto.post.PostCreateRequest;
import com.codeit.rest.dto.post.PostPageRequest;
import com.codeit.rest.dto.post.PostUpdateRequest;
import com.codeit.rest.entity.Category;
import com.codeit.rest.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    Post create(PostCreateRequest post, MultipartFile image);
    Post findById(Long id);
    List<Post> findAll();
    Post update(Long id, PostUpdateRequest updatePost, MultipartFile image);
    void delete(Long id);

    // 검색
    List<Post> findByTitle(String keyword);
    List<Post> findByContent(String keyword);
    List<Post> findByCategory(Category category);
    List<Post> findByTagsAny(List<String> tags);
    List<Post> findByAuthor(Long authorId);

    // 페이지 전용 (1-based page)
    Page<Post> findPage(PostPageRequest page);
    // 커스텀 SimplePageResponse 처리용 서비스
    SimplePageResponse<Post> findPage2(PostPageRequest page);

    boolean exists(Long id);
    long count();
}
