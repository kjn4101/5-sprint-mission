package com.codeit.blog.mapper;

import com.codeit.blog.dto.comment.CommentCreateRequest;
import com.codeit.blog.dto.comment.CommentResponse;
import com.codeit.blog.entity.Comment;
import com.codeit.blog.entity.Post;
import com.codeit.blog.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "content", source = "request.content")
    @Mapping(target = "author", source = "author")
    @Mapping(target = "post", source = "post")
    Comment toEntity(CommentCreateRequest request, User author, Post post);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Comment toEntity(CommentCreateRequest request);

    CommentResponse toResponse(Comment comment);

    List<CommentResponse> toResponseList(List<Comment> comments);
}
