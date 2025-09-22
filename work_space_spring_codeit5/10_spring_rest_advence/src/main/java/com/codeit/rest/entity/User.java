package com.codeit.rest.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    private Long id;
    private String username;

    @JsonIgnore // 직렬화 대상에서 제외하는것 : 안보이게끔 변경됨
    private String password;
    private String email;
    private String nickname;
    private LocalDate birthday;
    private Instant createdAt;
    private Instant updatedAt;
}
