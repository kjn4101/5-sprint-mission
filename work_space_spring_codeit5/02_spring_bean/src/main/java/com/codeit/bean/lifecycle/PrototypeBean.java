package com.codeit.bean.lifecycle;


import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype") // Bean으로 선언 될때 마다 매번 생성되는 Type
public class PrototypeBean {
    public static int count = 0;

    public PrototypeBean() {
        System.out.println("PrototypeBean이 생성되었습니다. count : " + ++count + ", " + this);
    }

    public String message(){
        return "PrototypeBean이 생성되었습니다. count : " + count;
    };

    // 빈이 생성된 이후에 호출되는 bean의 hook(콜백함수)
    @PostConstruct
    public void init(){
        System.out.println("PrototypeBean의 init 호출 count : " + count);
    }

    // Bean이 소멸되이 이전에 호출되는 bean의 hook (호출시기 : tomcat 내려갈때)
    @PreDestroy
    public void destroy(){
        System.out.println("PrototypeBean의 destroy 호출" );
    }

}
