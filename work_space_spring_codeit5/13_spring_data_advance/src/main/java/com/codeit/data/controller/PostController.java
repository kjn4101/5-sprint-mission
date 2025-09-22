package com.codeit.data.controller;


import com.codeit.data.dto.base.ApiResult;
import com.codeit.data.dto.base.SimplePageResponse;
import com.codeit.data.dto.post.PostCreateRequest;
import com.codeit.data.dto.post.PostPageRequest;
import com.codeit.data.dto.post.PostSearchRequest;
import com.codeit.data.dto.post.PostUpdateRequest;
import com.codeit.data.entity.Comment;
import com.codeit.data.entity.Post;
import com.codeit.data.service.CommentService;
import com.codeit.data.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {
    private final PostService postService;
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<ApiResult<Post>> create(@RequestBody PostCreateRequest req) {
        Post created = postService.create(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResult.ok(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<Post>> findById(@PathVariable Long id) {
        Post found = postService.findById(id);
        return ResponseEntity.ok(ApiResult.ok(found));
    }

    @GetMapping
    public ResponseEntity<ApiResult<List<Post>>> findAll() {
        List<Post> posts = postService.findAll();
        return ResponseEntity.ok(ApiResult.ok(posts));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<Post>> update(
            @PathVariable Long id,
            @RequestBody PostUpdateRequest req
    ) {
        Post updated = postService.update(id, req);
        return ResponseEntity.ok(ApiResult.ok(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<Void>> delete(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<ApiResult<List<Post>>> findTitle(@PathVariable String title) {
        return ResponseEntity.ok(ApiResult.ok(postService.findTitle(title)));
    }

    @GetMapping("/tag/{tag}")
    public ResponseEntity<ApiResult<List<Post>>> findTag(@PathVariable String tag) {
        return ResponseEntity.ok(ApiResult.ok(postService.findTag(tag)));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResult<List<Post>>> findCategory(@PathVariable String category) {
        return ResponseEntity.ok(ApiResult.ok(postService.findCategory(List.of(category))));
    }

    @GetMapping("/category")
    public ResponseEntity<ApiResult<List<Post>>> findCategory(@RequestParam List<String> category) {
        return ResponseEntity.ok(ApiResult.ok(postService.findCategory(category)));
    }


    @GetMapping("/search")
    public ResponseEntity<ApiResult<SimplePageResponse<Post>>> findPage(
            @ModelAttribute  PostPageRequest pageReq,
            @ModelAttribute  PostSearchRequest searchReq) {
        return ResponseEntity.ok(ApiResult.ok(postService.findPage(pageReq, searchReq)));
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResult<Long>> count() {
        return ResponseEntity.ok(ApiResult.ok(postService.count()));
    }


    // 특정 게시글(Post)의 댓글 조회
    @GetMapping("/{postId}/comments")
    public ResponseEntity<ApiResult<List<Comment>>> findByPost(@PathVariable Long postId) {
        List<Comment> comments = commentService.findByPostId(postId);
        return ResponseEntity.ok(ApiResult.ok(comments));
    }

}
