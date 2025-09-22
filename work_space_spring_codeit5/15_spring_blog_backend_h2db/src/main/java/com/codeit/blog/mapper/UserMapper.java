package com.codeit.blog.mapper;

import com.codeit.blog.dto.post.PostResponse;
import com.codeit.blog.dto.user.UserCreateRequest;
import com.codeit.blog.dto.user.UserResponse;
import com.codeit.blog.dto.user.UserSimpleResponse;
import com.codeit.blog.entity.Post;
import com.codeit.blog.entity.User;
import org.mapstruct.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toUser(UserCreateRequest request);

    UserSimpleResponse toUserSimpleResponse(User user);
    UserResponse toUserDetailResponse(User user);
    List<UserResponse> toUserResponseList(List<User> posts);

    @AfterMapping
    default void setAvatarUrl(User user, @MappingTarget UserResponse response) {
        if(user.isHasAvatar()){
            String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/avatars/")
                    .toUriString();
            response.setAvatarUrl(baseUrl + user.getUsername());
        }
    }
}
