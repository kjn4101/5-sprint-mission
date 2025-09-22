package com.codeit.aop.service;

import com.codeit.aop.dto.user.UserCreateRequest;
import com.codeit.aop.dto.user.UserUpdateRequest;
import com.codeit.aop.entity.User;
import com.codeit.aop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService{

    private final UserRepository userRepository;

    public User create(UserCreateRequest newUser) {
        User user = newUser.toUser();
        if(user.getUsername() == null) {
            throw new IllegalArgumentException("새 사용자는 username이 필수입니다.");
        }
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        return userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(
                ()-> new NoSuchElementException("사용자를 찾을 수 없습니다. id=" + id));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                ()-> new NoSuchElementException("사용자를 찾을 수 없습니다. username=" + username)
        );
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User update(Long id, UserUpdateRequest updateUser) {
        User user = findById(id);
        user.setPassword(updateUser.getPassword());
        user.setNickname(updateUser.getNickname());
        user.setEmail(updateUser.getEmail());
        user.setBirthday(updateUser.getBirthday());
        user.setUpdatedAt(Instant.now());
        return userRepository.save(user);
    }

    public User updateAll(Long id, UserCreateRequest newUser) {
        User user = findById(id);
        user.setUsername(newUser.getUsername());
        user.setPassword(newUser.getPassword());
        user.setNickname(newUser.getNickname());
        user.setEmail(newUser.getEmail());
        user.setBirthday(newUser.getBirthday());
        user.setUpdatedAt(Instant.now());
        return userRepository.save(user);
    }

    public void delete(Long id) {
        if(!userRepository.existsById(id)) {
            throw new NoSuchElementException("삭제 대상이 없습니다. id : " + id);
        }
        userRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    public long count() {
        return userRepository.count();
    }
}
