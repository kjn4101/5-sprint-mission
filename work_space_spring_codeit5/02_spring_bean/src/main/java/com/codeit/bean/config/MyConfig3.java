package com.codeit.bean.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfig3 {

    // 별칭 사용
    @Bean("mainDB")
    public Configurer maindbConfigurer(){
        Configurer configurer = new Configurer();
        configurer.setOption("Main DB 설정 값");
        System.out.println("생성 : " + configurer);
        return configurer;
    }

    @Bean(name = "subDB")
    public Configurer subdbConfigurer(){
        Configurer configurer = new Configurer();
        configurer.setOption("Sub DB 설정 값");
        System.out.println("생성 : " + configurer);
        return configurer;
    }
    
    // @Qualifier : bean의 별칭을 통해 주입 할 bean을 정하는 어노테이션
    @Bean(name = {"combi", "dual"})
    public Configurer combiConfigurer2(@Qualifier("mainDB") Configurer maindbConfigurer,
                                      @Qualifier("subDB") Configurer subdbConfigurer) {
        Configurer configurer = new Configurer();
        configurer.setOption(maindbConfigurer.getOption() + ", " + subdbConfigurer.getOption());
        System.out.println("생성 : " + configurer);
        return configurer;
    }

}
