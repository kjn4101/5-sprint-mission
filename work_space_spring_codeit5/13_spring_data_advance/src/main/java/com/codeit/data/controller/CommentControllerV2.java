package com.codeit.data.controller;

import com.codeit.data.dto.base.ApiResult;
import com.codeit.data.dto.base.SlicePageResponse;
import com.codeit.data.dto.comment.CommentCreateRequest;
import com.codeit.data.dto.comment.CommentResponse;
import com.codeit.data.dto.comment.CommentSimpleResponse;
import com.codeit.data.service.CommentService;
import com.codeit.data.service.CommentServiceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/comments")
public class CommentControllerV2 {

    private final CommentService commentService;
    private final CommentServiceV2 commentServiceV2;

    // mapper 반영 예제

    // 댓글 생성 → CommentResponse 반환
    @PostMapping
    public ResponseEntity<ApiResult<CommentResponse>> create(@RequestBody CommentCreateRequest request) {
        CommentResponse saved = commentServiceV2.saveComment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResult.ok(saved));
    }

    // 댓글 단건 조회 → CommentResponse 반환
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<CommentResponse>> findById(@PathVariable Long id) {
        CommentResponse found = commentServiceV2.findById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult.ok(found));
    }

    // 모든 댓글 조회1 - mapper 적용
    @GetMapping()
    public ResponseEntity<ApiResult<List<CommentResponse>>> findAll2() {
        List<CommentResponse> comments = commentServiceV2.findAll();
        return ResponseEntity.ok(ApiResult.ok(comments));
    }


    // 모든 댓글 조회2 -> slice 적용 예제
    @GetMapping("/slice")
    public ResponseEntity<ApiResult<SlicePageResponse<CommentResponse>>> findAllSlice(
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long cursor
    ) {
        SlicePageResponse<CommentResponse> body = commentServiceV2.findAllSlice(size, cursor);
        return ResponseEntity.ok(ApiResult.ok(body));
    }


    // 댓글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<Void>> delete(@PathVariable Long id) {
        commentService.deleteById(id);
        return ResponseEntity.ok(ApiResult.ok(null, "댓글이 삭제되었습니다"));
    }
}
