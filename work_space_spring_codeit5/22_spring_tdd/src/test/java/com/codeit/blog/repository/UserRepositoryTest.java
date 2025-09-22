package com.codeit.blog.repository;

import com.codeit.blog.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

//@SpringBootTest
@DataJpaTest // jpa에 관련 최소 bean만 load 하는 어노테이션
@ActiveProfiles("test") // test yml 활성화
public class UserRepositoryTest {
    
    @Autowired
    private UserRepository userRepository; // 검증할 대상

    @Autowired
    private TestEntityManager em; // 트랜잭션을 컨트롤 하기 위한 manager

    // 고정 픽스처 생성
    private User createUser(String username, String email){
        return User.builder()
                .username(username)
                .password("password123")
                .email(email)
                .nickname("스프링마스터")
                .hasAvatar(false)
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
    }

    @Test
    @DisplayName("uesrname으로 사용자 조회")
    void findByUsernameTest(){
        // given : 테스트에 필요한 테스트셋을 만드는 단계
        userRepository.save(createUser("codeit1", "codeit1@eamil.com"));
        em.flush(); // 강제로 commit을 수행하는 단계
        em.clear(); // 캐시를 비우는 명령

        // when : 테스트 대상을 찾아오는 단계
        Optional<User> found = userRepository.findByUsername("codeit1");

        // then : 테스트 검증을 수행하는 단계
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("codeit1");
        assertThat(found.get().getEmail()).isEqualTo("codeit1@eamil.com");

        // test를 수행하면 자동으로 rollback 실행됨!
    }

    @Test
    @DisplayName("없는 username 조회시 빈 Optional 확인")
    void findByUsernameTest2(){
        assertThat(userRepository.findByUsername("no_user")).isEmpty();
    }
    
    @Test
    @DisplayName("email로 사용자 조회")
    void findByEmailTest(){
        // given
        userRepository.save(createUser("codeit2", "codeit2@email.com"));
        em.flush(); em.clear();

        // when
        Optional<User> found = userRepository.findByEmail("codeit2@email.com");

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("codeit2");
        assertThat(found.get().getEmail()).isEqualTo("codeit2@email.com");
    }

    @Test
    @DisplayName("username 중복 여부 확인")
    void findByUsernameTest3(){
        // given
        userRepository.save(createUser("codeit3", "codeit3@email.com"));
        em.flush(); em.clear();

        // then
        assertThat(userRepository.existsByUsername("codeit3")).isTrue();
        assertThat(userRepository.existsByUsername("no_user2")).isFalse();
    }

    @Test
    @DisplayName("email 중복 여부 확인")
    void existsByEmail() {
        // given
        userRepository.save(createUser("codeit4", "codeit4@example.com"));
        em.flush(); em.clear();

        // then
        assertThat(userRepository.existsByEmail("codeit4@example.com")).isTrue();
        assertThat(userRepository.existsByEmail("none@example.com")).isFalse();
    }

}
















