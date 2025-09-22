package com.codeit.blog.basic;

import org.junit.jupiter.api.*;


import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 같은 테스트 인스턴스를 재사용! 인스턴스 한번만 생성함
//@TestMethodOrder(MethodOrderer.Random.class) // 랜덤으로 순서 정함!
//@TestMethodOrder(MethodOrderer.DisplayName.class) // default 옵션!
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // @Order 기준으로 순서 지정!
public class Basic03Test {
    // 만일 공유하고싶다면? static 권장!
    private /*static*/ int counter = 0;

    @Test // @Test 어노테이션의 특징은 각각을 독립적인 환경에서 객체를 생성함으로 멤버변수가 공유되지 않는다!
    void test1(){
        counter++;
        System.out.println("counter1 = " + counter);
    }

    @Test
    void test3(){
        counter++;
        System.out.println("counter3 = " + counter);
    }

    @Test
    void test2(){
        counter++;
        System.out.println("counter2 = " + counter);
    }

    // 2) 실행순서 지정 예시
    @Test @Order(2)
    void secondTest() { System.out.println("Second"); }

    @Test @Order(1)
    void firstTest() { System.out.println("First"); }

    @Test @Order(3)
    void thirdTest() { System.out.println("Third"); }

    // 3) 중첩 테스트 + 태깅
    @Nested
    class MathTests{
        @Test void additionTest(){assertEquals(5, 3+2);}
        @Test void divisionTest(){assertEquals(2, 10 / 5);}
    }

    @Test
    @Tag("slow")
    void slow() throws InterruptedException {
        Thread.sleep(1000);
        assertTrue(true);
    }

}
