package com.codeit.blog.repository;

import com.codeit.blog.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"author", "post"})
    List<Comment> findAll();

    // 특정 블로그에 달린 댓글 조회
    @EntityGraph(attributePaths = {"author", "post"})
    List<Comment> findByPostId(Long postId);

    // 특정 작성자가 단 댓글 조회
    @EntityGraph(attributePaths = {"author"})
    List<Comment> findByAuthorId(Long authorId);

    // Slice 적용
    // 첫 페이지: 최신순
    @EntityGraph(attributePaths = {"author"})
    Slice<Comment> findAllByOrderByIdDesc(Pageable pageable);

    // 다음 페이지: keyset(커서) 기반
    @EntityGraph(attributePaths = {"author"})
    Slice<Comment> findAllByIdLessThanOrderByIdDesc(Long lastId, Pageable pageable);
}
