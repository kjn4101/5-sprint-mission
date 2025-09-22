package com.codeit.rest.controller;


import com.codeit.rest.dto.common.ApiResult;
import com.codeit.rest.entity.Post;
import com.codeit.rest.service.PostService;
import com.codeit.rest.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

// Spring HATEOAS 예제
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/posts")
@Tag(name = "Post", description = "Post API") // tag까진 필수!
public class PostControllerV2 {
    private final PostService postService;
    private final UserService userService;
    
    // HATEOAS 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<EntityModel<Post>>> findById(@PathVariable Long id) {
        Post post = postService.findById(id);
        EntityModel<Post> model = EntityModel.of(post,
            linkTo(methodOn(PostControllerV2.class).findById(id)).withSelfRel(),
            linkTo(methodOn(PostControllerV2.class).findById(id + 1)).withRel("next"),
            linkTo(methodOn(PostControllerV2.class).findAll()).withRel("posts")
        );
        if(post.getAuthorId() != null){
            model.add(linkTo(methodOn(UserControllerV2.class).findById(id)).withRel("author"));
        }
        return ResponseEntity.ok(ApiResult.ok(model));
    }

    // HATEOAS 전체 조회
    @GetMapping
    public CollectionModel<EntityModel<Post>> findAll() {
        List<EntityModel<Post>> posts = postService.findAll().stream()
                .map(p -> EntityModel.of(
                        p,
                        linkTo(methodOn(PostControllerV2.class).findById(p.getId())).withSelfRel()
                ))
                .toList();

        return CollectionModel.of(
                posts,
                linkTo(methodOn(PostControllerV2.class).findAll()).withSelfRel()
        );
    }
}
