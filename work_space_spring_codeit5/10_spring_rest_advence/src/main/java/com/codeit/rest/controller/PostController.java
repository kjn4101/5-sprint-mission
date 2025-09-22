package com.codeit.rest.controller;


import com.codeit.rest.config.FileConfig;
import com.codeit.rest.dto.common.ApiResult;
import com.codeit.rest.dto.common.SimplePageResponse;
import com.codeit.rest.dto.post.PostCreateRequest;
import com.codeit.rest.dto.post.PostPageRequest;
import com.codeit.rest.dto.post.PostUpdateRequest;
import com.codeit.rest.entity.Category;
import com.codeit.rest.entity.Post;
import com.codeit.rest.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@Tag(name = "Post", description = "Post API") // tag까진 필수!
public class PostController {
    private final PostService postService;
    private final FileConfig fileConfig;

    @Operation(summary = "Post 생성", description = "새로운 사용자를 생성합니다.") // 메서드에 대한 설명을 다는 Swagger 어노테이션
    // 패턴1 - 파일 유첨이 있을때 특수하게 처리해야하는 패턴
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // 파일 유첨이 필요할때
    public ResponseEntity<ApiResult<Post>> create(
            @Parameter(description = "게시글 JSON", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
            @RequestPart PostCreateRequest post, // RequestPart : input에 json 으로 받아올때 사용하는 어노테이션
            @RequestParam(required = false) MultipartFile image) {
        Post createdPost = postService.create(post, image);
        return ResponseEntity.status(HttpStatus.CREATED) // 201
                .body(ApiResult.ok(createdPost));
    }

    @Operation(summary = "Post 생성(json)", description = "새로운 사용자를 생성합니다. json으로만 입력 받습니다.")
    // 패턴1 - 파일 유첨이 없을때 순수 REST로 설계할수 있는 패턴
    @PostMapping(value = "/json", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResult<Post>> createForJson(
            @RequestBody PostCreateRequest post// RequestBody : form이 아닌 순수 json 으로 입력 받을때
    ) {
        Post createdPost = postService.create(post, null);
        return ResponseEntity.status(HttpStatus.CREATED) // 201
                .body(ApiResult.ok(createdPost));
    }

    // 패턴2
    // 해당 패턴의 장점 : 모든 interface를 form로 맞춰 설계할수 있다.
    @PostMapping(value = "/form", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResult<Post>> createForMultiForm(
            @ModelAttribute PostCreateRequest post, // ModelAttribute : 진짜 form 처리, input을 각각의 이름으로 활용,
            @RequestParam(required = false) MultipartFile image) {
        Post createdPost = postService.create(post, image);
        return ResponseEntity.status(HttpStatus.CREATED) // 201
                .body(ApiResult.ok(createdPost));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<Post>> get(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResult.ok(postService.findById(id)));
    }


    // 쿼리 처리 받아주는 곳
    // 1. findAll
    // 2. findByTitle / findByContent ...
    // -> 5가지 중 하나만 처리하는 방식으로
    @GetMapping
    public ResponseEntity<ApiResult<List<Post>>> searchPost(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) Category category, // enum으로 받고 싶을때
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false) Long authorId
    ) {

        List<Post> result = null;
        if(title != null && !title.isEmpty()){
            result = postService.findByTitle(title);
        } else if(content != null && !content.isEmpty()){
            result = postService.findByContent(content);
        } else if(category != null ){
            result = postService.findByCategory(category);
        } else if(tags != null && !tags.isEmpty()){
            result = postService.findByTagsAny(tags);
        }else if(authorId != null){
            result = postService.findByAuthor(authorId);
        } else{
            result = postService.findAll();
        }
        return ResponseEntity.ok(ApiResult.ok(result));
    }

    @GetMapping("/page")
    public ResponseEntity<ApiResult<Page<Post>>> searchPostPage(PostPageRequest postPageRequest) {
        return ResponseEntity.ok(ApiResult.ok(postService.findPage(postPageRequest)));
    }

    @GetMapping("/page2")
    public ResponseEntity<ApiResult<SimplePageResponse<Post>>> searchPostPage2(PostPageRequest postPageRequest) {
        return ResponseEntity.ok(ApiResult.ok(postService.findPage2(postPageRequest)));
    }

    // 수정 (multipart/form-data)
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResult<Post>> update(
            @PathVariable Long id,
            @RequestPart("post") PostUpdateRequest reqBody,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        Post updated = postService.update(id, reqBody, image);
        return ResponseEntity.ok(ApiResult.ok(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<Void>> delete(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.ok(ApiResult.ok("post가 정상적으로 삭제되었습니다."));
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResult<Long>> count() {
        return ResponseEntity.ok(ApiResult.ok(postService.count()));
    }

    // 파일 다운로드
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadByPostId(@PathVariable Long id) throws IOException {
        Post post = postService.findById(id);
        if (post.getImagePath() == null || post.getImagePath().isBlank()) {
            return ResponseEntity.notFound().build();
        }
        Path uploadRoot = Paths.get(fileConfig.getUploadDir()).toAbsolutePath().normalize();
        Path filePath = uploadRoot.resolve(post.getImagePath()).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.notFound().build();
        }

        String contentType = Files.probeContentType(filePath);
        if (!StringUtils.hasText(contentType)) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        String downloadName = StringUtils.hasText(post.getImageRealName())
                ? post.getImageRealName()
                : resource.getFilename();

        String encoded = URLEncoder.encode(downloadName, StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");
        String contentDisposition = "attachment; filename=\"" + downloadName + "\"; filename*=UTF-8''" + encoded;

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .contentLength(Files.size(filePath))
                .body(resource);
    }

}














