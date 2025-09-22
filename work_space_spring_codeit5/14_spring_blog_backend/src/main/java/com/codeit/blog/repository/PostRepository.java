package com.codeit.blog.repository;

import com.codeit.blog.entity.Category;
import com.codeit.blog.entity.Post;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    // 제목/내용/태그 부분검색
    List<Post> findByTitleContaining(String keyword);
    List<Post> findByContentContaining(String keyword);
    List<Post> findByTagsContainingIgnoreCase(String tags);

    // 카테고리별 블로그 찾기
    List<Post> findByCategory(Category category);
    List<Post> findByCategoryIn(Collection<Category> categories);

    // 작성자 ID로 블로그 찾기
    List<Post> findByAuthor_Id(Long authorId);
    List<Post> findByAuthor_IdOrderByIdDesc(Long authorId);
    List<Post> findByAuthor_NicknameContaining(String nickname);



    // 작성자 이름(username)으로 게시글 조회
    @Query("""
            select p
            from Post p
            join fetch p.author u
            where u.username = :username
            """)
    List<Post> findPostsByUsername(@Param("username") String username);


    // 페이징 예시 - 제목 like
    @Query("""
            select p
            from Post p
            where p.title like %:kw%
            """)
    Page<Post> findPageByTitleJPQL(@Param("kw") String keyword, Pageable pageable);


    // 모든 조건 AND 검색
    @Query("""
            select p
            from Post p
            join fetch p.author u
            where (:title   is null or p.title   like %:title%)
              and (:content is null or p.content like %:content%)
              and (:nickname is null or u.nickname like %:nickname%)
            order by p.id desc
            """)
    Page<Post> searchPostsAnd(
            @Param("title") String title,
            @Param("content") String content,
            @Param("nickname") String nickname,
            Pageable pageable);

    // 모든 조건 AND 검색 + Slice
    @Query("""
            select p
            from Post p
            join fetch p.author u
            where (:title   is null or p.title   like %:title%)
              and (:content is null or p.content like %:content%)
              and (:nickname is null or u.nickname like %:nickname%)
            """)
    Slice<Post> searchPostsAnd2(
            @Param("title") String title,
            @Param("content") String content,
            @Param("nickname") String nickname,
            Pageable pageable);


    // N+1 이슈 해결하기
    // 1. EntityGraph에 JOIN 해야할 엔티티 변수명 명시
    @Override
    @EntityGraph(attributePaths = {"author", "comments", "attachments"})
    List<Post> findAll(@Nullable Sort sort);

    @Override
    @EntityGraph(attributePaths = {"author", "comments", "attachments"})
    Optional<Post> findById(@Nullable Long id);


    // N+1 이슈 해결하기2
    // 2. Fetch Join 활용하기 
    @Query("""
        select distinct p
        from Post p
        join fetch p.author
        left join fetch p.comments c
        left join fetch p.attachments a
        order by p.id desc
    """)
    List<Post> findAllV2();

}
