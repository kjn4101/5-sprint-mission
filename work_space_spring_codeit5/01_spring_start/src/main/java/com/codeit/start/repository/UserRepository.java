package com.codeit.start.repository;

import com.codeit.start.domain.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// 리포지토리임을 알리는 어노테이션, + Bean 생성
@Repository
public class UserRepository {
    private final List<User> list = new ArrayList<>();
    
    public UserRepository() {
        list.add(new User(1L,"test1", "1234", "홍길동"));
        list.add(new User(2L,"test2", "1234", "박길동"));
        list.add(new User(3L,"test3", "1234", "최길동"));
    }

    public User save(User user){
        findById(user.getId()).ifPresent(list::remove);
        list.add(user);
        return user;
    }

    public Optional<User> findById(Long id){
        return list.stream().filter(x -> x.getId().equals(id)).findFirst();
    }

    public List<User> findAll(){
       return list;
    }

    public long count(){
        return list.size();
    }

    public void delete(Long id){
        list.removeIf(x -> x.getId().equals(id));
    }

    public boolean existsById(Long id){
        return list.stream().anyMatch(x -> x.getId().equals(id));
    }
}
