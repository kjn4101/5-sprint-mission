package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

public class FileUserService implements UserService {
    private final Map<UUID, User> users = new HashMap<>();

    public void saveUserData(List<User> user) {
        try (FileOutputStream fos = new FileOutputStream("User.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<User> loadUserData() {
        File file = new File("User.ser");
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (FileInputStream fis = new FileInputStream("User.ser");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (List<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public FileUserService() {
        List<User> loaded = loadUserData();
        for (User user : loaded) {
            users.put(user.getId(), user);
        }
    }

    @Override
    public User createUser(String username, String email, String password) {
        for (User u : users.values()) {
            if (u.getUsername().equals(username)) {
                System.out.println("이미 존재하는 사용자명입니다.");
                return u;
            }
        }
        User user = new User(username, email, password);
        users.put(user.getId(), user);
        saveUserData(new ArrayList<>(users.values()));
        System.out.println("사용자가 생성되었습니다: " + user);
        return user;
    }

    @Override
    public User findUserById(UUID id) {
        User user = users.get(id);
        if (user == null) {
            System.out.println("해당 ID의 사용자가 없습니다.");
        }
        return user;
    }

    @Override
    public User findUser(String username) {
        for (User user : users.values()) {
            if (user.getUsername().equals(username)) return user;
        }
        System.out.println("해당 사용자명이 존재하지 않습니다.");
        return null;
    }

    @Override
    public List<User> findAllUsers() {
        if (users.isEmpty()) {
            System.out.println("등록된 사용자가 없습니다.");
        }
        return new ArrayList<>(users.values());
    }

    @Override
    public void updateUser(UUID id, String newUsername, String newPassword) {
        User user = users.get(id);
        if (user == null) {
            System.out.println("수정할 사용자가 없습니다.");
            return;
        }
        user.update(newUsername, newPassword);
        saveUserData(new ArrayList<>(users.values()));
        System.out.println("사용자가 수정되었습니다: " + user);
    }

    @Override
    public void deleteUser(UUID id) {
        User removed = users.remove(id);
        if (removed == null) {
            System.out.println("삭제할 사용자가 없습니다.");
        } else {
            saveUserData(new ArrayList<>(users.values()));
            System.out.println("사용자가 삭제되었습니다: " + removed);
        }
    }
}
