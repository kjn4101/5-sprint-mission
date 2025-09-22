package com.codeit.rest.controller;


import com.codeit.rest.dto.common.ApiResult;
import com.codeit.rest.entity.Post;
import com.codeit.rest.entity.User;
import com.codeit.rest.service.UserService;
import com.codeit.rest.service.impl.PostServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/users")
@Tag(name = "User", description = "User API") // tag까진 필수!
public class UserControllerV2 {

    private final UserService userService;
    private final PostServiceImpl postService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<User>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResult.ok(userService.findById(id)));
    }

    // 중첩 리소스 표현1 -> 내가 쓴글 보기, 내가 주문한 상품 보기...
    @GetMapping("/{userId}/posts")
    public ResponseEntity<ApiResult<List<Post>>> findAllPosts(@PathVariable Long userId) {
        User user = userService.findById(userId);
        List<Post> posts = postService.findByAuthor(userId);
        // 임시코드
        posts.forEach(post -> {post.setAuthor(user);});
        return ResponseEntity.ok(ApiResult.ok(posts));
    }

    // 중첩 리소스 표현2 -> {userID}/posts/{postId} // 상세보기
    @GetMapping("/{userId}/posts/{postId}")
    public ResponseEntity<ApiResult<Post>> findPostById(@PathVariable Long userId, @PathVariable Long postId) {
        User user = userService.findById(userId);
        List<Post> posts = postService.findByAuthor(user.getId());

        Post post = posts.stream()
                .filter(p -> p.getId() != null && p.getId().equals(postId))
                .findFirst()
                .orElse(null);
        user.setBirthday(null);

        if(post == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiResult.fail("POST_NOT_FOUND", "해당 사용자의 블로그를 찾을수 없습니다."));
        }
        post.setAuthor(user);
        return ResponseEntity.ok(ApiResult.ok(post));
    }
}
