package com.codeit.di.repository.impl;


import com.codeit.di.domain.User;
import com.codeit.di.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


// 일반 DB
@Repository("userRepository")
public class UserRepositoryImpl implements UserRepository {

    private final List<User> list = new ArrayList<>();

    public UserRepositoryImpl() {
        list.add(new User(1L, "test1", "1234", "홍길동", "user"));
        list.add(new User(2L, "test2", "1234", "최길동", "user"));
        list.add(new User(3L, "test3", "1234", "김길동", "user"));
    }

    public User save(User user){
        findById(user.getId()).ifPresent(list::remove);
        list.add(user);
        return user;
    }

    public Optional<User> findById(Long id){
        return list.stream().filter(user -> user.getId().equals(id)).findFirst();
    }

    public Optional<User> findByUsername(String username){
        return list.stream().filter(user -> user.getUsername().equals(username)).findFirst();
    }

    public List<User> findAll(){
        return list;
    }

    public long count(){
        return list.size();
    }

    public void delete(Long id){
        list.removeIf(user -> user.getId().equals(id));
    }

    public boolean existsById(Long id){
        return list.stream().anyMatch(user -> user.getId().equals(id));
    }

}
