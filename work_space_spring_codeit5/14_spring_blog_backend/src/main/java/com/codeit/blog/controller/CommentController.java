package com.codeit.blog.controller;

import com.codeit.blog.dto.base.ApiResult;
import com.codeit.blog.dto.base.SlicePageResponse;
import com.codeit.blog.dto.comment.CommentCreateRequest;
import com.codeit.blog.dto.comment.CommentResponse;
import com.codeit.blog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentService commentService;

    // mapper 반영 예제

    // 댓글 생성
    @PostMapping
    public ResponseEntity<ApiResult<CommentResponse>> create(@RequestBody CommentCreateRequest request) {
        CommentResponse saved = commentService.saveComment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResult.ok(saved));
    }

    // 댓글 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<CommentResponse>> findById(@PathVariable Long id) {
        CommentResponse found = commentService.findById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult.ok(found));
    }

    // 모든 댓글 조회1
    @GetMapping()
    public ResponseEntity<ApiResult<List<CommentResponse>>> findAll2() {
        List<CommentResponse> comments = commentService.findAll();
        return ResponseEntity.ok(ApiResult.ok(comments));
    }


    // 모든 댓글 조회2
    @GetMapping("/slice")
    public ResponseEntity<ApiResult<SlicePageResponse<CommentResponse>>> findAllSlice(
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long cursor
    ) {
        SlicePageResponse<CommentResponse> body = commentService.findAllSlice(size, cursor);
        return ResponseEntity.ok(ApiResult.ok(body));
    }

    // 댓글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<Void>> delete(@PathVariable Long id) {
        commentService.deleteById(id);
        return ResponseEntity.ok(ApiResult.ok(null, "댓글이 삭제되었습니다"));
    }
}
