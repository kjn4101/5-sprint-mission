package com.codeit.rest.repository.impl;

import com.codeit.rest.entity.User;
import com.codeit.rest.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository("userRepository")
public class UserRepositoryImpl implements UserRepository {
    private final List<User> userList = new ArrayList<>();
    private long seq = 1L;

    {
        userList.add(User.builder().id(seq++).username("test01").password("1234").email("test01@email.com").nickname("스프링마스터").birthday(LocalDate.of(1992, 3, 14)).createdAt(Instant.now().minusSeconds(86400L * 5)).build());
        userList.add(User.builder().id(seq++).username("test02").password("1234").email("test02@email.com").nickname("야근하는개발자").birthday(LocalDate.of(1995, 7, 22)).createdAt(Instant.now().minusSeconds(86400L * 4)).build());
        userList.add(User.builder().id(seq++).username("test03").password("1234").email("test03@email.com").nickname("판교러").birthday(LocalDate.of(1998, 11, 9)).createdAt(Instant.now().minusSeconds(86400L * 3)).build());
        userList.add(User.builder().id(seq++).username("test04").password("1234").email("test04@email.com").nickname("코드장인").birthday(LocalDate.of(2001, 5, 3)).createdAt(Instant.now().minusSeconds(86400L * 2)).build());
        userList.add(User.builder().id(seq++).username("test05").password("1234").email("test05@email.com").nickname("디버깅요정").birthday(LocalDate.of(2004, 9, 17)).createdAt(Instant.now().minusSeconds(86400L)).build());
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(seq++);
            user.setCreatedAt(Instant.now());
            userList.add(user);
        } else {
            user.setUpdatedAt(Instant.now());
            deleteById(user.getId());
            userList.add(user);
        }
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return userList.stream()
                .filter(u -> Objects.equals(u.getId(), id))
                .findFirst();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userList.stream()
                .filter(u -> Objects.equals(u.getUsername(), username))
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userList);
    }

    @Override
    public void deleteById(Long id) {
        userList.removeIf(u -> Objects.equals(u.getId(), id));
    }

    @Override
    public boolean existsById(Long id) {
        return userList.stream().anyMatch(u -> Objects.equals(u.getId(), id));
    }

    @Override
    public long count() {
        return userList.size();
    }
}
