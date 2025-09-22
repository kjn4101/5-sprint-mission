package com.codeit.bean.lifecycle;

public class MyBean {

    public static int count = 0;

    public MyBean() {
        System.out.println("MyBean 인스턴스 생성됨: " + this + " count: " + ++count);
    }

    // 초기화 로직
    public void init() {
        System.out.println("MyBean 초기화 로직 실행");
    }

    // 소멸 로직
    public void cleanup() {
        System.out.println("MyBean 소멸 로직 실행");
    }

    public String message() {
        return "MyBean 인스턴스입니다: " + this + " count: " + count;
    }
}