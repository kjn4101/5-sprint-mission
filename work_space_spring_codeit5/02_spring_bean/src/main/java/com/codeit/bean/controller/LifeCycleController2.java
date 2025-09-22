package com.codeit.bean.controller;


import com.codeit.bean.lifecycle.MyBean;
import com.codeit.bean.lifecycle.PrototypeBean;
import com.codeit.bean.lifecycle.SingletonBean;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LifeCycleController2 {

    private final MyBean myBean;
    private final PrototypeBean prototypeBean;
    private final SingletonBean singletonBean;

    // 싱글톤이 내부에서 프로토타입을 요청할 때마다 새로운 인스턴스 생성
    @GetMapping("/test-singleton2")
    public String testSingleton() {
        return singletonBean.message() +"<br>"+ myBean.message();
    }

    // 컨트롤러가 직접 프로토타입을 주입받았을 때 (컨트롤러 생성 시점의 1개 인스턴스)
    @GetMapping("/test-prototype2")
    public String testPrototype() {
        return prototypeBean.message();
    }

}
