package com.codeit.blog.service;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest // Spring context 환경에서 테스트 할때 어노테이션
@ActiveProfiles("test") // yml 파일 활성화
public class UserServiceTest2 {

    @Autowired // 의존성을 주입하는 어노테이션
    private UserService userService;

    @Test
    public void findAll(){
        var list = userService.findAll();
        System.out.println(list);
        assert !list.isEmpty();
    }
}
