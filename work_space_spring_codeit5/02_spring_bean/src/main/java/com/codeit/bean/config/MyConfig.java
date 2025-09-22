package com.codeit.bean.config;


// 무언가 설정하는 클래스 (DB, JPA, Log, Security ...)

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // 설정 파일임을 알리는 어노테이션 이자 Bean으로 관리
public class MyConfig {

    
    @Bean // 해당 메서드 이름으로 bean을 생성하는 어노테이션
    public Configurer myConfigurer(){ // bean 이름 : myConfigurer
        Configurer configurer = new Configurer();
        configurer.setOption("option");
        System.out.println("생성 : " + configurer);
        return configurer;
    }

}
