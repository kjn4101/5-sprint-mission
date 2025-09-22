package com.codeit.blog.dto.user;

import lombok.*;

import java.time.LocalDate;


@Data @Builder @ToString
public class UserCreateRequest2{
     private  String username;
     @ToString.Exclude
     private  String password;
     private  String email;
     private  String nickname;
     private  LocalDate birthday;
}
