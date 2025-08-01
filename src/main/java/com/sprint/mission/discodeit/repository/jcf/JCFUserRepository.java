package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> users = new HashMap<>();


    @Override
    public void save(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public User findById(UUID id) {
        return users.get(id);
    }

    @Override
    public User findByUsername(String username) {
        for (User user : users.values()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void update(UUID id, String newUsername, String newPassword) {
        User user = users.get(id);
        if (user != null) {
            user.update(newUsername, newPassword);
        }
    }

    @Override
    public void delete(UUID id) {
        users.remove(id);
    }
}
