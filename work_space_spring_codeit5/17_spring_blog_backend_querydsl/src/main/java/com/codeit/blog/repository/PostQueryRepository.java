// src/main/java/com/codeit/blog/repository/PostQueryRepository.java
package com.codeit.blog.repository;

import com.codeit.blog.dto.post.PostFlatDto;
import com.codeit.blog.entity.Category;
import com.codeit.blog.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PostQueryRepository {

    // 1. findById
    Optional<Post> findByIdQ(Long id);

    // 2. findAll 플랫화
    List<PostFlatDto> findAllFlat();

    // 3. 복합 검색 (타이틀/컨텐츠/작성자/카테고리/tag/댓글내용/댓글작성자)
    List<Post> search(String title, String content, String author,
                      Category category, String tag,
                      String commentContent, String commentAuthor);

    // 4. 복합 검색 + 페이징 (정렬은 기본 id desc)
    Page<Post> searchPage(String title, String content, String author,
                          Category category, String tag,
                          String commentContent, String commentAuthor,
                          Pageable pageable);

    // 5. 복합 검색 + 페이징 + pageable 정렬 반영(화이트리스트)
    Page<Post> searchPageSorted(String title, String content, String author,
                                Category category, String tag,
                                String commentContent, String commentAuthor,
                                Pageable pageable);
}
