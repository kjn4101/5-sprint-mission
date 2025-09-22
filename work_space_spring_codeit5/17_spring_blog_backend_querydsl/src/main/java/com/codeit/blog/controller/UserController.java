package com.codeit.blog.controller;

import com.codeit.blog.dto.base.ApiResult;
import com.codeit.blog.dto.comment.CommentResponse;
import com.codeit.blog.dto.post.PostResponse;
import com.codeit.blog.dto.user.UserCreateRequest;
import com.codeit.blog.dto.user.UserResponse;
import com.codeit.blog.dto.user.UserUpdateRequest;
import com.codeit.blog.entity.User;
import com.codeit.blog.service.CommentService;
import com.codeit.blog.service.PostService;
import com.codeit.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final CommentService commentService;
    private final PostService postService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResult<UserResponse>> create(
            @RequestPart("user") UserCreateRequest user,
            @RequestPart(value = "avatar", required = false) MultipartFile avatar) {
        UserResponse created = userService.create(user,avatar);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResult.ok(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<UserResponse>> findById(@PathVariable Long id) {
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

    @PutMapping(value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResult<UserResponse>> updatePut(
            @PathVariable Long id,
            @RequestPart("user") UserUpdateRequest user,
            @RequestPart(value = "avatar", required = false) MultipartFile avatarFile
    ) {
        return ResponseEntity.ok(
                ApiResult.ok(userService.update(id, user, avatarFile))
        );
    }

    @PatchMapping(value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResult<UserResponse>> updatePatch(
            @PathVariable Long id,
            @RequestPart("user") UserUpdateRequest user,
            @RequestPart(value = "avatar", required = false) MultipartFile avatarFile
    ) {
        return ResponseEntity.ok(
                ApiResult.ok(userService.update(id, user, avatarFile))
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<User>> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok(ApiResult.ok(null, "사용자가 삭제되었습니다."));
    }

    // 특정 사용자(User)가 작성한 블로그 조회
    @GetMapping("/{userId}/posts")
    public ResponseEntity<ApiResult<List<PostResponse>>> findPostByUser(@PathVariable Long userId) {
        List<PostResponse> posts = postService.findByAuthorId(userId);
        return ResponseEntity.ok(ApiResult.ok(posts));
    }
    
    // 특정 사용자(User)가 작성한 댓글 조회
    @GetMapping("/{userId}/comments")
    public ResponseEntity<ApiResult<List<CommentResponse>>> findCommentByUser(@PathVariable Long userId) {
        List<CommentResponse> comments = commentService.findByAuthorId(userId);
        return ResponseEntity.ok(ApiResult.ok(comments));
    }
    
}
