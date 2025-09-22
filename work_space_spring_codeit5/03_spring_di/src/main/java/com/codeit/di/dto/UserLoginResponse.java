package com.codeit.di.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserLoginResponse {
    private String username;
    private String name;
    private String roles;
    private String message;
}
