package com.codeit.data.repository;

import com.codeit.data.dto.post.PostDetailResponse;
import com.codeit.data.dto.post.PostSimpleResponse;
import com.codeit.data.entity.Category;
import com.codeit.data.entity.Post;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 제목, 내용, 태그 부분 검색
    List<Post> findByTitleContaining(String title); // like %title%
    List<Post> findByContentContaining(String content);
    List<Post> findByTagsContainingIgnoreCase(String tags);
    List<Post> findByTitleContainingOrderByIdDesc(String title); // 정렬

    // 카테고리 별 블로그 찾기
    List<Post> findByCategory(Category category);
    List<Post> findByCategoryIn(List<Category> categories);

    // 작성자 ID로 블로그 찾기
    List<Post> findByAuthorId(Long id);
    List<Post> findByAuthor_Id(Long id);

    @EntityGraph(attributePaths = {"author"})
    List<Post> findByAuthor_IdOrderByIdDesc(Long id); // 정렬
    List<Post> findByAuthor_NicknameContaining(String nickname);


    // JPQL : JPA에서 제공하는 범용 유사 쿼리 (실제 DB 쿼리로 번역되어 실행 됨)
    // -> 쿼리메서드 이름과 겹치지 않게끔 만드는게 유리하다.
    // 쿼리는 모두 소문자로 작성된다.

    // 작성자 이름(username)으로 게시글 검색
//    @Query("select p from Post p") // 간단한 SELECT
    @Query("""
            select p
            from Post p
            join p.author u
            where u.username = :username
            """)
    List<Post> findPostsByUsername(@Param("username") String username);

    // 제목 like 절(% %) + 페이징 처리
    @Query("""
            select p
            from Post p
            where p.title like %:keyword%
    """)
    Page<Post> findPageByTitle(@Param("keyword") String keyword, Pageable pageable);
    
    // 제목 + 내용 like 절(% %) + 정렬처리
    @Query("""
            select p
            from Post p
            where p.title like %:title% or p.content like %:content%
    """)
    List<Post> findPageByTitleAndContent(@Param("title") String title,
                                         @Param("content") String content, Sort sort);


    // 모든 조건 AND절 검색 (title, content, nickname) + 페이징처리 ★★★★★★ -> 쿼리메서드로 해결이 어렵다.
    @Query("""
        select p
        from Post p
        join p.author u
        where (:title is null or p.title like %:title%)
        and (:content is null or p.content like %:content%)
        and (:nickname is null or u.nickname like %:nickname%)
        order by p.id desc
    """)
    Page<Post> searchPosts(@Param("title") String title,
                           @Param("content") String content,
                           @Param("nickname") String nickname,
                           Pageable pageable);

    // Native SQL
    // 사용자와 게시글 JOIN 하여 조회
    @Query(value = """
            SELECT p.*, u.*
            FROM posts p
            JOIN users u ON p.author_id = u.id
            WHERE u.username = :username
            """, nativeQuery = true)
    List<Post> findPostsByUsernameNative(@Param("username") String username);

    // N+1 문제 해결하기1
    // 1. EntityGraph에 통해 Post에 적힌 JOIN 할 엔티티 "변수명" 명시
    //    private List<Comment> "comments";
    //    private User "author";
    @Override
    @EntityGraph(attributePaths = {"author", "comments"})
    List<Post> findAll(@Nullable Sort sort);

    // N+1 문제 해결방법2
    // 2. Fetch Join 활용하기
    @Query("""
            select p
            from Post p
            join fetch p.author u
            left join fetch p.comments c
            order by p.id desc
            """)
    List<Post> findAllV2();

    // N+1 문제 해결방법3 -> QueryDSL로 작성해야할 코드
    // 3. DTO 프로젝션 적용하기
    // -> 반환될 Entity를 DTO로 변경하여 반환하는 방법
    // -> Front 화면에서 필요한 값만 필터링 할때도 사용하는 기법으로 매우중요! ★★★★★
    @Query("""
    select new com.codeit.data.dto.post.PostSimpleResponse(
        p.id,
        p.title,
        p.tags,
        p.category,
        p.createdAt,
        u.nickname
    )
    from Post p
    join p.author u
    order by p.id desc
    """)
    List<PostSimpleResponse> findAllV3();


    // detail 개선 버전
    @Query("""
    select new com.codeit.data.dto.post.PostDetailResponse(
        p.id,
        p.title,
        p.content,
        p.tags,
        p.category,
        p.createdAt,
        u.nickname,
        null
    )
    from Post p
    join p.author u
    where p.id = :id
    order by p.id desc
    """)
    Optional<PostDetailResponse> findById2(@Param("id") Long id);
}













