package com.codeit.blog.repository.impl;

import com.codeit.blog.dto.post.PostFlatDto;
import com.codeit.blog.entity.*;
import com.codeit.blog.repository.PostQueryRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostQueryRepositoryImpl implements PostQueryRepository {

    private final JPAQueryFactory queryFactory;

    // join 할때 필요할 QType을 미리 선언
    private static final QPost p = QPost.post;
    private static final QUser u = QUser.user;
    private static final QComment c = QComment.comment;
    private static final QBinaryContent b = QBinaryContent.binaryContent;

    private static final QUser postAuthor = new QUser("postAuthor");
    private static final QUser commentAuthor = new QUser("commentAuthor");


    @Override
    public Optional<Post> findByIdQ(Long id) {
        Post post = queryFactory
                .selectDistinct(p)  // join 절에서 여러개가 조회 될수 있을때 중복 제거용
                .from(p)
                .join(p.author, postAuthor).fetchJoin() // join fetch 처리가 필요하면? fetchJoin
                .leftJoin(p.comments, c).fetchJoin() // leftJoin : 댓글이 없을때도 조회 될수 있도록
                .leftJoin(c.author, commentAuthor).fetchJoin() // comment의 글쓴이 가져오고 싶다면?
                .leftJoin(p.attachments, b).fetchJoin()
                .where(p.id.eq(id))
                .fetchOne();
        
        return Optional.ofNullable(post);
    }

    @Override
    public List<PostFlatDto> findAllFlat() {
        return queryFactory
                .select(Projections.constructor(
                        PostFlatDto.class,
                        p.id, p.title, p.createdAt, 
                        c.id.count(), // 댓글수
                        u.username, u.nickname))
                .from(p)
                .join(p.author, u)
                .leftJoin(p.comments, c)
                .groupBy(p.id)
                .orderBy(p.id.desc())
                .fetch();
    }

    @Override
    public List<Post> search(String title, String content, String author, Category category, String tag, String commentContent, String commentAuthorStr) {
        // 동적 쿼리 작성하는 방법
        BooleanBuilder where = new BooleanBuilder(); // where절을 묶어줄 빌더

        if(title != null && !title.isBlank()){
            where.and(p.title.contains(title));
//            where.and(p.title.containsIgnoreCase(title));
//            where.and(p.title.startsWith(title));
//            where.and(p.title.endsWith(title));
        }
        if (content != null && !content.isBlank()) {
            where.and(p.content.contains(content));
        }
        if (author != null && !author.isBlank()) {
            where.and(postAuthor.username.eq(author).or(postAuthor.nickname.eq(author)));
        }
        if (category != null) {
            where.and(p.category.eq(category));
        }
        if (tag != null && !tag.isBlank()) {
            where.and(p.tags.containsIgnoreCase(tag));
        }
        if (commentContent != null && !commentContent.isBlank()) {
            where.and(c.content.contains(commentContent));
        }
        if (commentAuthorStr != null && !commentAuthorStr.isBlank()) {
            where.and(commentAuthor.username.eq(commentAuthorStr).or(commentAuthor.nickname.eq(commentAuthorStr)));
        }

        return queryFactory
                .selectDistinct(p)  // join 절에서 여러개가 조회 될수 있을때 중복 제거용
                .from(p)
                .join(p.author, postAuthor).fetchJoin() // join fetch 처리가 필요하면? fetchJoin
                .leftJoin(p.comments, c).fetchJoin() // leftJoin : 댓글이 없을때도 조회 될수 있도록
                .leftJoin(c.author, commentAuthor).fetchJoin() // comment의 글쓴이 가져오고 싶다면?
                .leftJoin(p.attachments, b).fetchJoin()
                .where(where)
                .orderBy(p.id.desc())
                .fetch();
    }

    @Override
    public Page<Post> searchPage(String title, String content, String author,
                                 Category category, String tag,
                                 String commentContent, String commentAuthorStr,
                                 Pageable pageable) {

        BooleanBuilder where = new BooleanBuilder();

        if (title != null && !title.isBlank()) {
            where.and(p.title.containsIgnoreCase(title));
        }
        if (content != null && !content.isBlank()) {
            where.and(p.content.containsIgnoreCase(content));
        }
        if (category != null) {
            where.and(p.category.eq(category));
        }
        if (tag != null && !tag.isBlank()) {
            where.and(p.tags.containsIgnoreCase(tag));
        }
        if (author != null && !author.isBlank()) {
            where.and(postAuthor.username.containsIgnoreCase(author)
                    .or(postAuthor.nickname.containsIgnoreCase(author)));
        }
        if (commentContent != null && !commentContent.isBlank()) {
            where.and(c.content.containsIgnoreCase(commentContent));
        }
        if (commentAuthorStr != null && !commentAuthorStr.isBlank()) {
            where.and(commentAuthor.username.containsIgnoreCase(commentAuthorStr)
                    .or(commentAuthor.nickname.containsIgnoreCase(commentAuthorStr)));
        }

        // 콘텐츠 쿼리 (to-many fetch join + 페이징은 일반적으로 비권장)
        List<Post> contentRows = queryFactory
                .selectDistinct(p)
                .from(p)
                .join(p.author, postAuthor).fetchJoin()
                .leftJoin(p.comments, c).fetchJoin()
                .leftJoin(c.author, commentAuthor).fetchJoin()
                .leftJoin(p.attachments, b).fetchJoin()
                .where(where)
                .orderBy(p.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 카운트 쿼리(가벼운 조인만, fetchJoin 금지)
        Long total = queryFactory
                .select(p.id.countDistinct())
                .from(p)
                .leftJoin(p.author, postAuthor)
                .leftJoin(p.comments, c)
                .leftJoin(c.author, commentAuthor)
                .where(where)
                .fetchOne();

        return new PageImpl<>(contentRows, pageable, total == null ? 0 : total);
    }

    @Override
    public Page<Post> searchPageSorted(String title, String content, String author,
                                       Category category, String tag,
                                       String commentContent, String commentAuthorStr,
                                       Pageable pageable) {

        BooleanBuilder where = new BooleanBuilder();

        if (title != null && !title.isBlank()) {
            where.and(p.title.containsIgnoreCase(title));
        }
        if (content != null && !content.isBlank()) {
            where.and(p.content.containsIgnoreCase(content));
        }
        if (category != null) {
            where.and(p.category.eq(category));
        }
        if (tag != null && !tag.isBlank()) {
            where.and(p.tags.containsIgnoreCase(tag));
        }
        if (author != null && !author.isBlank()) {
            where.and(postAuthor.username.containsIgnoreCase(author)
                    .or(postAuthor.nickname.containsIgnoreCase(author)));
        }
        if (commentContent != null && !commentContent.isBlank()) {
            where.and(c.content.containsIgnoreCase(commentContent));
        }
        if (commentAuthorStr != null && !commentAuthorStr.isBlank()) {
            where.and(commentAuthor.username.containsIgnoreCase(commentAuthorStr)
                    .or(commentAuthor.nickname.containsIgnoreCase(commentAuthorStr)));
        }

        OrderSpecifier<?> order;
        if (pageable == null || pageable.getSort().isUnsorted()) {
            order = p.id.desc();
        } else {
            Sort.Order so = pageable.getSort().stream()
                    .findFirst()
                    .orElse(Sort.Order.desc("id"));
            boolean asc = so.isAscending();
            String key = so.getProperty();
            if ("id".equals(key)) {
                order = asc ? p.id.asc() : p.id.desc();
            } else if ("createdAt".equals(key)) {
                order = asc ? p.createdAt.asc() : p.createdAt.desc();
            } else if ("title".equals(key)) {
                order = asc ? p.title.asc() : p.title.desc();
            } else {
                order = p.id.desc(); // 화이트리스트 밖 → 기본
            }
        }

        // 콘텐츠 쿼리
        List<Post> contentRows = queryFactory
                .selectDistinct(p)
                .from(p)
                .join(p.author, postAuthor).fetchJoin()
                .leftJoin(p.comments, c).fetchJoin()
                .leftJoin(c.author, commentAuthor).fetchJoin()
                .leftJoin(p.attachments, b).fetchJoin()
                .where(where)
                .orderBy(order)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 카운트 쿼리
        Long total = queryFactory
                .select(p.id.countDistinct())
                .from(p)
                .leftJoin(p.author, postAuthor)
                .leftJoin(p.comments, c)
                .leftJoin(c.author, commentAuthor)
                .where(where)
                .fetchOne();

        return new PageImpl<>(contentRows, pageable, total == null ? 0 : total);
    }
}
