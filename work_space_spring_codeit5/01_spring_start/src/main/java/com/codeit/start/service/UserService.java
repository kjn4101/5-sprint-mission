package com.codeit.start.service;

import com.codeit.start.domain.User;
import com.codeit.start.event.UserRegisteredEvent;
import com.codeit.start.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Service임을 알리는 어노테이션 + Bean 생성
@Service
public class UserService {

    @Autowired // 필드 DI 주입하는 방법, 레거시에서 표준이었음, 5.x부터 권장하지 않음.
    private UserRepository userRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher; // 이벤트를 받아 중계하는 객체

    public User register(User user){
        User saveUser = userRepository.save(user);
        // 회원 가입 이벤트 발생 시점!
        eventPublisher.publishEvent( new UserRegisteredEvent(this, saveUser));
        // -> 설정 안하면 동기로 UserEventListener로 이벤트가 전달되어 실행됨.
        // 비동기를 위해선  @Async와 관련 Configuration이 필요한데, 후반부에 다시 교육할예정
        return saveUser;
    }

    public Optional<User> getUserById(Long id){
        return userRepository.findById(id);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        userRepository.delete(id);
    }
}
