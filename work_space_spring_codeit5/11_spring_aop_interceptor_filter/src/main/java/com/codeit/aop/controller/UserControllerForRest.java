package com.codeit.aop.controller;


import com.codeit.aop.dto.common.ApiResult;
import com.codeit.aop.dto.user.UserCreateRequest;
import com.codeit.aop.dto.user.UserUpdateRequest;
import com.codeit.aop.entity.User;
import com.codeit.aop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
//@RequestMapping("/api/v1/users") // 버전 분할 가능!
@RequestMapping("/api/users")
public class UserControllerForRest {

    private final UserService userService;

    // @RequestBody : 대상 객체가 json, xml로 구성되었을때 자동으로 파싱해주는 어노테이션
    @PostMapping
    public ResponseEntity<ApiResult<User>> createUser(@RequestBody UserCreateRequest user) {
        User createdUser = userService.create(user);
//        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(createdUser));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResult.ok(createdUser,"사용자가 정상적으로 생성되었습니다."));
    }

    // @PathVariable : 경로상의 식별자가 있는 경우 매개변수로 가져오는 어노테이션
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<User>> findUserById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResult.ok(userService.findById(id)));
    }

    // @RequestParam : query 처리할때 사용하는 어노테이션
    @GetMapping
    public ResponseEntity<ApiResult<List<User>>> findAll(
            @RequestParam(required = false) String username
    ) {
        if(username != null && !username.isEmpty()){
            return ResponseEntity.ok(ApiResult.ok(List.of(userService.findByUsername(username))));
        }else{
            return ResponseEntity.ok(ApiResult.ok(userService.findAll()));
        }
    }

    // Update
    @PutMapping("/{id}") // PutMapping : 값 전체가 새로운 값으로 대체될때
    public ResponseEntity<ApiResult<User>> updatePut(
            @PathVariable Long id,
            @RequestBody UserCreateRequest user) {
        return ResponseEntity.ok(ApiResult.ok(userService.updateAll(id, user)));
    }

    // Update
    @PatchMapping("/{id}") // PatchMapping :부분 갱신할때
    public ResponseEntity<ApiResult<User>> updateUser(
            @PathVariable Long id,
            @RequestBody UserUpdateRequest user) {
        return ResponseEntity.ok(ApiResult.ok(userService.update(id, user)));
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<Void>> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok(ApiResult.ok("사용자가 정상적으로 삭제 되었습니다."));
    }

}
