package com.codeit.data.mapper;

import com.codeit.data.dto.comment.CommentResponse;
import com.codeit.data.dto.post.PostResponseForComment;
import com.codeit.data.dto.user.UserCreateRequest;
import com.codeit.data.dto.user.UserResponse;
import com.codeit.data.entity.Comment;
import com.codeit.data.entity.Post;
import com.codeit.data.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

//    @Mapping(target = "id", ignore = true) // 특정 맵핑을 무시할때 사용하는 어노테이션
//    @Mapping(target = "createdAt", ignore = true)
//    @Mapping(target = "updatedAt", ignore = true)
    User toUser(UserCreateRequest request);

    UserResponse toUserResponse(User user);
}
