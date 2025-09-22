package com.codeit.blog.entity;

import com.codeit.blog.entity.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "users")
@Getter @Setter @SuperBuilder @ToString(callSuper = true)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // JPA를 위한 기본 생성자
public class User extends BaseEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String username;   // 로그인용 아이디

    @JsonIgnore
    @Column(nullable = false, length = 200)
    private String password;   // 비밀번호 (응답 시 제외)

    @Column(nullable = false, unique = true, length = 100)
    private String email;      // 이메일

    @Column(nullable = false, length = 50)
    private String nickname;   // 닉네임

    @Column(nullable = false)
    private boolean hasAvatar = false;
}