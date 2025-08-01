package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

public class BasicUserService implements UserService {

    private final UserRepository userRepository;

    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public BasicUserService() {
        this.userRepository = new FileUserRepository();
    }

    @Override
    public User createUser(String username, String email, String password) {
        User existing = userRepository.findByUsername(username);
        if (existing != null) {
            System.out.println("이미 존재하는 사용자명입니다.");
            return existing;
        }

        User user = new User(username, email, password);
        userRepository.save(user);
        System.out.println("사용자가 생성되었습니다: " + user);
        return user;
    }

    @Override
    public User findUserById(UUID id) {
        User user = userRepository.findById(id);
        if (user == null) {
            System.out.println("해당 ID의 사용자가 없습니다.");
        }
        return user;
    }

    @Override
    public User findUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            System.out.println("해당 사용자명이 존재하지 않습니다.");
        }
        return user;
    }

    @Override
    public List<User> findAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            System.out.println("등록된 사용자가 없습니다.");
        }
        return users;
    }

    @Override
    public void updateUser(UUID id, String newUsername, String newPassword) {
        User user = userRepository.findById(id);
        if (user == null) {
            System.out.println("수정할 사용자가 없습니다.");
            return;
        }
        user.update(newUsername, newPassword);
        userRepository.update(id, newUsername, newPassword);
        System.out.println("사용자가 수정되었습니다: " + user);
    }

    @Override
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id);
        if (user == null) {
            System.out.println("삭제할 사용자가 없습니다.");
            return;
        }
        userRepository.delete(id);
        System.out.println("사용자가 삭제되었습니다: " + user);
    }
}
