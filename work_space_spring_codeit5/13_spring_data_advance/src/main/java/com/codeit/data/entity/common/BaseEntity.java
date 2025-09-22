package com.codeit.data.entity.common;


// Entity에서 공통적인 부분을 담을 인터페이스

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter @ToString
@MappedSuperclass // 공통 Entity를 알리는 어노테이션
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 보안적인 용도
@SuperBuilder // lombok용 자식이 부모 빌더를 접근할수 있도록 돕는 어노테이션
public abstract class BaseEntity {

    @Id // 테이블의 PK임을 알리는 어노테이션
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // PK가 자동으로 증가되는데, DB에게 위임한다는 어노테이션
    @Column(name = "id", updatable = false, nullable = false) // 이름이 불일치하면 name으로 컬럼명을 적는다.
    private Long id;

    @CreatedDate // 자동으로 생성되는 날짜임을 알리는 어노테이션
    @Column(name = "created_at", updatable = false, nullable = false)
    private Instant createdAt;

    @LastModifiedDate // 마지막 갱신되는 시간으로 갱신되는 어노테이션
    @Column(nullable = false)
    private Instant updatedAt;
}


















