package com.codeit.data.mapper;

import com.codeit.data.dto.post.PostCreateRequest;
import com.codeit.data.entity.Post;
import com.codeit.data.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "author", source = "author")
    Post toPost(PostCreateRequest request, User author);

    Post toPost(PostCreateRequest request);
}
