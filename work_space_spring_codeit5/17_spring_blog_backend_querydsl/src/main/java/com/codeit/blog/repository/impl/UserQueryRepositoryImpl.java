package com.codeit.blog.repository.impl;

import com.codeit.blog.entity.QUser;
import com.codeit.blog.entity.User;
import com.codeit.blog.repository.UserQueryRepository;
import com.codeit.blog.repository.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class UserQueryRepositoryImpl implements UserQueryRepository {

    private final JPAQueryFactory queryFactory;
    private static final QUser u = QUser.user;

    @Override
    public Optional<User> findByIdQ(Long id) {
        User user = queryFactory
                .selectFrom(u) // 전체 컬럼 조회
                .where(u.id.eq(id)) // where 절
                .fetchOne(); // 실행하는데 하나만 가져올때

        return Optional.ofNullable(user);
    }

    @Override
    public List<User> findAllOrderByCreatedDesc() {
        List<User> list = queryFactory
                .selectFrom(u)
                .orderBy(u.createdAt.desc(), u.id.desc(), u.email.asc())
                .fetch();

        return list;
    }

    @Override
    public Optional<User> findByLoginId(String usernameOrEmail) {
        if(usernameOrEmail == null || usernameOrEmail.isBlank()){
            return Optional.empty();
        }

        User user = queryFactory
                .selectFrom(u)
                .where(u.username.eq(usernameOrEmail).or(u.email.eq(usernameOrEmail)))
                .fetchOne();

        return Optional.ofNullable(user);
    }
}
