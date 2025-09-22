package com.codeit.blog.entity;

import com.codeit.blog.entity.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "posts")
@Getter
@Setter
@SuperBuilder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 기본 생성자 보호 수준
public class Post extends BaseEntity {

    @Column(nullable = false, length = 1000)
    private String title;

    @Column(nullable = false, length = 5000)
    private String content;

    @Column(length = 500)
    private String tags;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Category category;

    // 작성자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    // 댓글
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();


    // 첨부 파일용 - post_attachments 조인 테이블 이용
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinTable(
            name = "post_attachments",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "attachment_id")
    )
    @Builder.Default
    private Set<BinaryContent> attachments = new LinkedHashSet<>();

}
