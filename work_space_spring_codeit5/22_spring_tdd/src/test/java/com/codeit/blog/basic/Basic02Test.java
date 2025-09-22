package com.codeit.blog.basic;


import org.junit.jupiter.api.Test;

// static import : Assertions을 생략하기 위해 권장!
import static org.junit.jupiter.api.Assertions.*;

// Assertions 활용 예제
public class Basic02Test {

    // 1) 기본 검증 메서드
    @Test
    void testBasicAssertions() {
        assert 1 == 1; // assert 키워드로 검증 가능하다!
        assertEquals(5, 2 + 3); // 값 비교
        assertNotEquals(5, 6);
        assertTrue("codeit".contains("codeit")); // 조건 확인
        assertNull(null);
        assertNotNull("test");
        assertArrayEquals(new int[]{1,2,3}, new int[]{1,2,3});
    }

    // 2) 그룹 검증
    @Test
    void testGroupedAssertions() {
        String name = "codeit";
        int age = 23;

        assertAll(
                ()->assertEquals(name, "codeit"),
                ()->assertEquals(age, 23),
                ()->assertTrue(name.contains("co"))
        );
    }

    // 3) 예외 검증
    @Test
    void testExceptionAssertions() {
        assertThrows(ArithmeticException.class, () -> divide(10, 0)); // 예외 발생
        assertDoesNotThrow(() -> divide(10, 2));
    }

    public int divide(int a, int b) {
        return a / b;
    }
}
