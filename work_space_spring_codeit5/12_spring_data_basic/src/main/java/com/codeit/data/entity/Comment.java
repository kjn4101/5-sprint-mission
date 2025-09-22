package com.codeit.data.entity;

import com.codeit.data.entity.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Entity
@Table(name = "comments")
@Getter @Setter @SuperBuilder /*@ToString*/
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Column(nullable = false, length = 1000)
    private String content;

    // JPA 에서 관계를 가지는 객체 설계하기
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)  // 외래키를 지정하는 어노테이션
    private User author;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    @JsonIgnore // json으로 만들때 제외하는 어노테이션
    private Post post; // 댓글이 달린 블로그
}

















