package com.codeit.data.entity;

import com.codeit.data.entity.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;


@Entity
@Table(name = "posts")
@Getter @Setter @SuperBuilder /*@ToString*/
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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

//    private String author_id; // 외래키 -> Entity에 들어가지 않는다.

    // JPA에서 관계를 가지는 객체 설계하기
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)  // 외래키를 지정하는 어노테이션
    private User author;

    // 댓글 -> 비주인일때 댓글을 가져오는 방법
    @OneToMany(mappedBy = "post")
    private List<Comment> comments;
}

















