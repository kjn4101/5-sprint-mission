package com.codeit.data.controller;

import com.codeit.data.dto.base.ApiResult;
import com.codeit.data.dto.comment.CommentCreateRequest;
import com.codeit.data.entity.Comment;
import com.codeit.data.service.CommentService;
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

    // 댓글 생성
    @PostMapping
    public ResponseEntity<ApiResult<Comment>> create(@RequestBody CommentCreateRequest request) {
        Comment saved = commentService.saveComment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResult.ok(saved));
    }

    // 댓글 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<Comment>> findById(@PathVariable Long id) {
        Comment found = commentService.findById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult.ok(found));

    }

    // 모든 댓글 조회
    @GetMapping
    public ResponseEntity<ApiResult<List<Comment>>> findAll() {
        List<Comment> comments = commentService.findAll();
        return ResponseEntity.ok(ApiResult.ok(comments));
    }


    // 댓글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<Void>> delete(@PathVariable Long id) {
        commentService.deleteById(id);
        return ResponseEntity.ok(ApiResult.ok(null, "댓글이 삭제되었습니다"));
    }


}
