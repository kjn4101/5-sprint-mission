package com.codeit.rest.repository;

import com.codeit.rest.entity.Category;
import com.codeit.rest.entity.Post;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Post save(Post post);
    Optional<Post> findById(Long id);
    List<Post> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
    long count();

    List<Post> findByTitleContaining(String keyword);
    List<Post> findByContentContaining(String keyword);
    List<Post> findByCategory(Category category);
    List<Post> findByTagsIn(List<String> tags);
    List<Post> findByAuthorId(Long authorId);
    
    // 페이지 설계
    Page<Post> findAllByPage(int size, int page);
}
