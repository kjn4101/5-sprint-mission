package com.codeit.blog.basic;

import org.junit.jupiter.api.*;

//@SpringBootTest // 반드시 필요한 어노테이션 아니다!!
public class Basic01Test {

    @BeforeAll
    public static void initAll() {
        System.out.println("BeforeAll - 전체 테스트 시작 전에 한번만 실행");
    }
    
    @BeforeEach 
    public void init(){
        System.out.println("BeforeEach - 각 테스트 시작 전에 실행");
    }
    
    @Test // 테스트 임을 알려주는 어노테이션
    @DisplayName("덧셈 테스트") // 해당 테스트가 무엇인지 설명해주는 보조 어노테이션
    void addTest() { // 테스트 메서드는 언더바 규칙을 사용해도 무관!!
        int result = 2 + 3;
        System.out.println("addTest");
        assert result == 5; // assert : 우항이 true면 테스트 성공!
        Assertions.assertEquals(5, result);
    }

    @Test
    @DisplayName("나눗셈 테스트")
    void divisionTest() {
        int result = 10 / 2;
        System.out.println("divisionTest");
        assert result == 5;
    }

    @AfterEach
    public void afterEach() {
        System.out.println("각 테스트 종료 후 실행");
    }

    @AfterAll
    public static void afterAll() {
        System.out.println("전체 테스트 종료 후 한번만 실행");
    }

}


















