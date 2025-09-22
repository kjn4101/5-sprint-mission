package com.codeit.blog.service;

import com.codeit.blog.config.FileConfig;
import com.codeit.blog.dto.user.UserCreateRequest;
import com.codeit.blog.dto.user.UserResponse;
import com.codeit.blog.dto.user.UserUpdateRequest;
import com.codeit.blog.entity.User;
import com.codeit.blog.mapper.UserMapper;
import com.codeit.blog.repository.UserRepository;
import com.codeit.blog.storage.FileStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService{
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final FileStorage fileStorage;


    @Transactional
    public UserResponse create(UserCreateRequest newUser, MultipartFile avatarFile) {
        User user = userMapper.toUser(newUser);
        if (user.getId() != null) {
            throw new IllegalArgumentException("새 사용자 생성 시 id는 null 이어야 합니다");
        }
        user = userRepository.save(user);
        if (avatarFile != null && !avatarFile.isEmpty()) {
            fileStorage.saveAvatarFile(user.getUsername(), avatarFile);
            user.setHasAvatar(true);
        }
        return userMapper.toUserDetailResponse(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다: " + id));
        return userMapper.toUserDetailResponse(user);
    }


    @Transactional(readOnly = true)
    public Optional<UserResponse> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toUserDetailResponse);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return userMapper.toUserResponseList(userRepository.findAll());
    }


    @Transactional
    public UserResponse update(Long id, UserUpdateRequest patch, MultipartFile avatarFile) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다: " + id));

        // 기본 정보 갱신
        user.setPassword(patch.getPassword());
        user.setEmail(patch.getEmail());
        user.setNickname(patch.getNickname());

        // 아바타 갱신
        if (avatarFile != null && !avatarFile.isEmpty()) {
            fileStorage.saveAvatarFile(user.getUsername(), avatarFile);
            user.setHasAvatar(true);
        }

        return userMapper.toUserDetailResponse(userRepository.save(user));
    }

    @Transactional
    public void delete(Long id) {
        User user = userRepository.findById(id).orElseThrow(()->new NoSuchElementException("삭제 대상 사용자가 없습니다: " + id));
        fileStorage.deleteAvatarFile(user.getUsername());
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean exists(Long id) {
        return userRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    public long count() {
        return userRepository.count();
    }


}
