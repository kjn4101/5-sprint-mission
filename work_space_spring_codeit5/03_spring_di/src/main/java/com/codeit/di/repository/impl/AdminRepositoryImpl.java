package com.codeit.di.repository.impl;


import com.codeit.di.domain.User;
import com.codeit.di.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// 다른 Table 관리되는 데이터 (저장소 다름)
@Repository("adminRepository")
public class AdminRepositoryImpl implements UserRepository {
    private final List<User> list = new ArrayList<>();

    public AdminRepositoryImpl() {
        list.add(new User(100L, "admin", "1234", "관리자", "admin"));
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
