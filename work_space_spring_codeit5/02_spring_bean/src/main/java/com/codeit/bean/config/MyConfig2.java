package com.codeit.bean.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfig2 {

    // 첫번째 Configurer
    @Bean
    public Configurer dbConfigurer(){
        Configurer configurer = new Configurer();
        configurer.setOption("DB 설정 값");
        System.out.println("생성 : " + configurer);
        return configurer;
    }

    // 두번째 Configurer
    @Bean
    public Configurer apiConfigurer(){
        Configurer configurer = new Configurer();
        configurer.setOption("API 설정 값");
        System.out.println("생성 : " + configurer);
        return configurer;
    }
    
    // 앞에 Config를 조합하는 CombiConfig 생성
    // Configurer가 여러 type이어도 메서드 이름으로 bean 식별이 가능!
    @Bean
    public Configurer combiConfigurer(Configurer dbConfigurer,
                                      Configurer apiConfigurer) {
        Configurer configurer = new Configurer();
        configurer.setOption(dbConfigurer.getOption() + ", " + apiConfigurer.getOption());
        System.out.println("생성 : " + configurer);
        return configurer;
    }

}
