package com.codeit.data.repository;

import com.codeit.data.dto.comment.CommentSimpleResponse;
import com.codeit.data.entity.Category;
import com.codeit.data.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 특정 블로그에 달린 댓글 조회 -> join절
    List<Comment> findByPostId(Long id);
    
    // 특정 블로그에 카데고리로 찾아오는 방법 -> join절 표현2
    List<Comment> findByPost_Category(Category post_category);

    // 특정 작성자(닉네임)가 단 댓글 조회
    List<Comment> findByAuthor_Nickname(String username);

    // 특정 작성자(ID)가 단 댓글 조회
    List<Comment> findByAuthorId(Long authorId);

    @Query("""
    select new com.codeit.data.dto.comment.CommentSimpleResponse(
        c.id, c.content, u.nickname, c.createdAt
    )
    from Comment c
    join c.post p
    join c.author u
    where p.id = :postId
    order by c.id desc
    """)
    List<CommentSimpleResponse> findCommentsByPostIdV2(@Param("postId") Long postId);


    // Slice 적용
    // 첫 페이지: 최신순
    Slice<Comment> findAllByOrderByIdDesc(Pageable pageable);

    // 다음 페이지: keyset(커서) 기반
    Slice<Comment> findAllByIdLessThanOrderByIdDesc(Long lastId, Pageable pageable);
}
