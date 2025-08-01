package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    void save(User user);
    User findById(UUID id);
    User findByUsername(String username);
    List<User> findAll();
    void update(UUID id, String newUsername, String newPassword);
    void delete(UUID id);


    // 더 필요한 내용은 아래 query 메서드 이름 형식을 참고하여 더 작성할 것
    // https://docs.spring.io/spring-data/jpa/reference/repositories/query-methods-details.html

}
