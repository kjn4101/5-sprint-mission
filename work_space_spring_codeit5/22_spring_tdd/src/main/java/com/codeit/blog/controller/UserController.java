package com.codeit.blog.controller;

import com.codeit.blog.dto.base.ApiResult;
import com.codeit.blog.dto.user.UserCreateRequest;
import com.codeit.blog.dto.user.UserResponse;
import com.codeit.blog.dto.user.UserUpdateRequest;
import com.codeit.blog.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResult<UserResponse>> create(
            @RequestPart("user") UserCreateRequest user,
            @RequestPart(value = "avatar", required = false) MultipartFile avatar) {
        UserResponse created = userService.create(user, avatar);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResult.ok(created));
    }

    @PostMapping("/post2")
    public ResponseEntity<ApiResult<UserResponse>> post2(
            @RequestParam @NotBlank @Size(min = 4, max = 20) String username,
            @RequestParam @NotBlank String password,
            @RequestParam @NotBlank String email,
            @RequestParam @NotBlank String nickname,
            @RequestParam @NotNull LocalDate birthday) {
        UserCreateRequest user = new UserCreateRequest(username, password, email, nickname, birthday);
        UserResponse created = userService.create(user, null);
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

    @PutMapping(value = "/{id}")
    public ResponseEntity<ApiResult<UserResponse>> updatePut(
            @PathVariable Long id,
            @RequestPart("user") @Valid UserUpdateRequest user,
            @RequestPart(value = "avatar", required = false) MultipartFile avatarFile
    ) {
        return ResponseEntity.ok(
                ApiResult.ok(userService.update(id, user, avatarFile))
        );
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<ApiResult<UserResponse>> updatePatch(
            @PathVariable Long id,
            @RequestPart("user") @Valid UserUpdateRequest user,
            @RequestPart(value = "avatar", required = false) MultipartFile avatarFile
    ) {
        return ResponseEntity.ok(
                ApiResult.ok(userService.update(id, user, avatarFile))
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<Void>> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok(ApiResult.ok(null, "사용자가 삭제되었습니다."));
    }

}
