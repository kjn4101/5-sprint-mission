package com.codeit.data.repository;


import com.codeit.data.entity.Category;
import com.codeit.data.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

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

}
