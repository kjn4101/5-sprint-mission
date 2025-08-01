package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class FileUserRepository implements UserRepository {
    private final String DIRECTORY = "User";
    private final String EXTENSION = ".ser";

    public FileUserRepository(){
        File dir = new File(DIRECTORY);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    private File getFile(UUID id){
        return new File(DIRECTORY + File.separator + id + EXTENSION);
    }

    @Override
    public void save(User user) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(getFile(user.getId())))) {
            oos.writeObject(user);
        } catch (IOException e) {
            throw new RuntimeException("유저 저장 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    @Override
    public User findById(UUID id) {
        File file = getFile(id);
        if (!file.exists()) return null;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (User) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("유저 불러오기 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    @Override
    public User findByUsername(String username) {
        for (User user : findAll()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        File dir = new File(DIRECTORY);
        File[] files = dir.listFiles((d, name) -> name.endsWith(EXTENSION));
        if (files == null) return users;

        for (File file : files) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                User user = (User) ois.readObject();
                users.add(user);
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("유저 파일 읽기 실패: " + file.getName());
            }
        }
        return users;
    }

    @Override
    public void update(UUID id, String newUsername, String newPassword) {
        User user = findById(id);
        if (user == null) {
            throw new NoSuchElementException("해당 ID의 사용자를 찾을 수 없습니다: " + id);
        }
        user.setUsername(newUsername);
        user.setPassword(newPassword);
        save(user);
    }

    @Override
    public void delete(UUID id) {
        File file = getFile(id);
        if (file.exists()) {
            if (!file.delete()) {
                throw new RuntimeException("유저 삭제에 실패했습니다: " + id);
            }
        }
    }

}
