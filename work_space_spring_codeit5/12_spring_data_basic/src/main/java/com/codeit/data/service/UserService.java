package com.codeit.data.service;

import com.codeit.data.dto.user.UserCreateRequest;
import com.codeit.data.dto.user.UserUpdateRequest;
import com.codeit.data.entity.User;
import com.codeit.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User create(UserCreateRequest newUser) {
        User user = newUser.toUser();
        if (user.getId() != null) throw new IllegalArgumentException("새 사용자 생성 시 id는 null 이어야 합니다");
        return userRepository.save(user);
    }


    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다: " + id));
    }


    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }


    @Transactional
    public User update(Long id, UserUpdateRequest patch) {
        User user = findById(id);
        user.setPassword(patch.getPassword());
        user.setEmail(patch.getEmail());
        user.setNickname(patch.getNickname());
        return userRepository.save(user);
    }

    @Transactional
    public User updateAll(Long id, UserCreateRequest newUser) {
        User user = findById(id);
        user.setUsername(newUser.getUsername());
        user.setPassword(newUser.getPassword());
        user.setEmail(newUser.getEmail());
        user.setNickname(newUser.getNickname());
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public void delete(Long id) {
        if (!userRepository.existsById(id)) throw new NoSuchElementException("삭제 대상 사용자가 없습니다: " + id);
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
