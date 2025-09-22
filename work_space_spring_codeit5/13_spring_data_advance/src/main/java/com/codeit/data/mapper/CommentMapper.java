package com.codeit.data.mapper;

import com.codeit.data.dto.comment.CommentCreateRequest;
import com.codeit.data.dto.comment.CommentResponse;
import com.codeit.data.dto.post.PostResponseForComment;
import com.codeit.data.entity.Comment;
import com.codeit.data.entity.Post;
import com.codeit.data.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "content", source = "request.content")
    @Mapping(target = "author", source = "author")
    @Mapping(target = "post", source = "post")
    Comment toEntity(CommentCreateRequest request, User author, Post post);

    // 여기서 별도로 필요한 mapper는 @Mapper어노테이션이 붙어있는 mapper에 어디서든 추가해야한다!
    CommentResponse toResponse(Comment comment);

    PostResponseForComment toPostResponseForComment(Post post);
}
