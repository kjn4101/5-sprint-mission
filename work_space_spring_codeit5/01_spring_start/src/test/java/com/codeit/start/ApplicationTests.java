package com.codeit.start;

import com.codeit.start.domain.User;
import com.codeit.start.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


// 회귀 테스트 용도로 필수로 활용하는 영역
// TDD 개발시에 커버리지 측정용으로도 활용하는 영역
@SpringBootTest
class ApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    void signUpSuccess() {
        User user =  new User(4L, "test4", "1234", "임길동");
        User registeredUser = userService.register(user);

        // 테스트를 수행하는 메서드 assertThat
        assertThat(registeredUser).isNotNull();
        assertThat(registeredUser.getName()).isEqualTo("임길동");
        assertThat(registeredUser.getId()).isEqualTo(4L);
    }

    @Test
    void getUser() {
        Optional<User> user = userService.getUserById(1L);
        assertThat(user.isPresent()).isEqualTo(true);
    }
}
