package com.codeit.blog.mapper;

import com.codeit.blog.dto.post.PostCreateRequest;
import com.codeit.blog.dto.post.PostResponse;
import com.codeit.blog.dto.post.PostOnlyIdResponse;
import com.codeit.blog.dto.post.PostSimpleResponse;
import com.codeit.blog.entity.Post;
import com.codeit.blog.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AttachFileMapper.class})
public interface PostMapper {

    @Mapping(target = "id", ignore = true) // 자동 생성 값으로 무시
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "author", source = "author")
    Post toPost(PostCreateRequest req, User author);

    PostResponse toPostResponse(Post post);
    PostSimpleResponse toPostSimpleResponse(Post post);
    PostOnlyIdResponse toPostPostOnlyIdResponse(Post post);

    List<PostResponse> toPostResponseList(List<Post> posts);

}
