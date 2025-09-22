package com.codeit.data.controller;

import com.codeit.data.dto.base.ApiResult;
import com.codeit.data.dto.user.UserCreateRequest;
import com.codeit.data.dto.user.UserUpdateRequest;
import com.codeit.data.entity.Comment;
import com.codeit.data.entity.Post;
import com.codeit.data.entity.User;
import com.codeit.data.service.CommentService;
import com.codeit.data.service.PostService;
import com.codeit.data.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final CommentService commentService;
    private final PostService postService;

    @PostMapping
    public ResponseEntity<ApiResult<User>> create(@RequestBody UserCreateRequest user) {
        User created = userService.create(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResult.ok(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<User>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResult.ok(userService.findById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResult<?>> findAll(
            @RequestParam(required = false) String username
    ) {
        if (username != null && !username.isBlank()) {
            return ResponseEntity.ok(ApiResult.ok(userService.findByUsername(username)));
        } else {
            return ResponseEntity.ok(ApiResult.ok(userService.findAll()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<User>> updatePut(@PathVariable Long id, @RequestBody UserCreateRequest user) {
        return ResponseEntity.ok(ApiResult.ok(userService.updateAll(id, user)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResult<User>> update(@PathVariable Long id, @RequestBody UserUpdateRequest patch) {
        return ResponseEntity.ok(ApiResult.ok(userService.update(id, patch)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<User>> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok(ApiResult.ok(null, "사용자가 삭제되었습니다."));
    }

    // 특정 사용자(User)가 작성한 블로그 조회
    @GetMapping("/{userId}/posts")
    public ResponseEntity<ApiResult<List<Post>>> findPostByUser(@PathVariable Long userId) {
        List<Post> posts = postService.findByAuthorId(userId);
        return ResponseEntity.ok(ApiResult.ok(posts));
    }
    
    // 특정 사용자(User)가 작성한 댓글 조회
    @GetMapping("/{userId}/comments")
    public ResponseEntity<ApiResult<List<Comment>>> findCommentByUser(@PathVariable Long userId) {
        List<Comment> comments = commentService.findByAuthorId(userId);
        return ResponseEntity.ok(ApiResult.ok(comments));
    }
    
}
