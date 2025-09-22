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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService{
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserResponse create(UserCreateRequest newUser) {
        User user = userMapper.toUser(newUser);
        if (user.getId() != null) {
            throw new UserException(ErrorCode.INVALID_USER_PARAMETER);
        }
        if(userRepository.existsByUsername(user.getUsername()) ){
            throw UserAlreadyExistsException.withUsername(user.getUsername());
        }
        try {
            user = userRepository.save(user);
        } catch (Exception e) {
            throw new UserException(ErrorCode.DUPLICATE_USER);
        }

        return userMapper.toUserDetailResponse(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> UserNotFoundException.withId(id.toString()));
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
    public UserResponse update(Long id, UserUpdateRequest patch) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> UserNotFoundException.withId(id.toString()));
        // 기본 정보 갱신
        user.setPassword(patch.password());
        user.setEmail(patch.email());
        user.setNickname(patch.nickname());
        user.setBirthday(patch.birthday());

        return userMapper.toUserDetailResponse(userRepository.save(user));
    }

    @Transactional
    public void delete(Long id) {
        User user = userRepository.findById(id).orElseThrow(()-> UserNotFoundException.withId(id.toString()));
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
