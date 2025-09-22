package com.codeit.blog.service;

import com.codeit.blog.dto.user.UserCreateRequest;
import com.codeit.blog.dto.user.UserResponse;
import com.codeit.blog.dto.user.UserUpdateRequest;
import com.codeit.blog.entity.User;
import com.codeit.blog.exception.ErrorCode;
import com.codeit.blog.exception.user.UserAlreadyExistsException;
import com.codeit.blog.exception.user.UserException;
import com.codeit.blog.exception.user.UserNotFoundException;
import com.codeit.blog.mapper.UserMapper;
import com.codeit.blog.repository.UserRepository;
import com.codeit.blog.storage.FileStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService{
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final FileStorage fileStorage;

    @Transactional
    public UserResponse create(UserCreateRequest newUser, MultipartFile avatarFile) {
        log.debug("사용자 생성 시작 : request={}", newUser);

        User user = userMapper.toUser(newUser);
        if (user.getId() != null) {
            throw new UserException(ErrorCode.INVALID_USER_PARAMETER);
        }
        if(userRepository.existsByUsername(user.getUsername()) ){
            throw UserAlreadyExistsException.withUsername(user.getUsername());
        }
        if (avatarFile != null && !avatarFile.isEmpty()) {
            fileStorage.saveAvatarFile(user.getUsername(), avatarFile);
            user.setHasAvatar(true);
        }
        try {
            user = userRepository.save(user);
        } catch (Exception e) {
            throw new UserException(ErrorCode.DUPLICATE_USER);
        }
        log.debug("사용자 생성 완료 : request={}", newUser);
        return userMapper.toUserDetailResponse(user);
    }

    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        log.debug("사용자 조회 시작 : id={}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> UserNotFoundException.withId(id.toString()));
        log.info("사용자 조회 완료 : id={}, username={}", id, user.getUsername());
        return userMapper.toUserDetailResponse(user);
    }


    @Transactional(readOnly = true)
    public Optional<UserResponse> findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                    .orElseThrow(()->UserNotFoundException.withUsername(username));

        return Optional.ofNullable(userMapper.toUserDetailResponse(user));
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return userMapper.toUserResponseList(userRepository.findAll());
    }


    @Transactional
    public UserResponse update(Long id, UserUpdateRequest patch, MultipartFile avatarFile) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> UserNotFoundException.withId(id.toString()));
        // 기본 정보 갱신
        user.setPassword(patch.password());
        user.setEmail(patch.email());
        user.setNickname(patch.nickname());
        user.setBirthday(patch.birthday());

        // 아바타 갱신
        if (avatarFile != null && !avatarFile.isEmpty()) {
            fileStorage.saveAvatarFile(user.getUsername(), avatarFile);
            user.setHasAvatar(true);
        }

        return userMapper.toUserDetailResponse(userRepository.save(user));
    }

    @Transactional
    public void delete(Long id) {
        log.debug("사용자 삭제 시작 : id={}", id);
        User user = userRepository.findById(id).orElseThrow(()-> UserNotFoundException.withId(id.toString()));
        userRepository.deleteById(id);
        fileStorage.deleteAvatarFile(user.getUsername());
        log.debug("사용자 삭제 완료 : id={}", id);
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
