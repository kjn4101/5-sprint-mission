package com.codeit.bean.lifecycle;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "singleton") // default 값 보통 생략됨!!
public class SingletonBean implements InitializingBean, DisposableBean {
    public static int count = 0;

    public SingletonBean() {
        System.out.println("SingletonBean 생성되었습니다. count : " + ++count + ", " + this);
    }

    public String message(){
        return "SingletonBean 생성되었습니다. count : " + count;
    };

    // 초기화 로직
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("SingletonBean 초기화 로직 실행");
    }

    // 소멸자
    @Override
    public void destroy() throws Exception {
        System.out.println("SingletonBean 소멸 로직 실행");
    }
}
