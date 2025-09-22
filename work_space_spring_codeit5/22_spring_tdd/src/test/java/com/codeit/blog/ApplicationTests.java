package com.codeit.blog;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// @SpringBootTest // Spring context를 load하는 어노테이션, 없는 경우 Spring bean을 사용할수 없음
//  -> 만일 Spring context 환경이 필요 없는 경우 생략하는 케이스도 존재!
class ApplicationTests {

    @Test // 해당 코드가 test 메서드 + class임을 알리는 어노테이션, Test 대상!!
    void contextLoads1() {
        assert 1 == 1; // true가 나오면 test 성공
    }

    @Test
    void contextLoads2() {
        assert 1 == 1; // false가 나오면 test 실패 -> 나중에 실패로 집계
    }

}
