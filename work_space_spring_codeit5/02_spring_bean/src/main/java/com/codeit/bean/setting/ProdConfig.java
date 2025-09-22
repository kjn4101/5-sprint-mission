package com.codeit.bean.setting;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
@Profile("prod") // prod 환경일때만 아래 DevConfig이 Bean으로 등록됨
public class ProdConfig {

    private final AppPropertiesProfile appProperties;

    @Bean
    public String serverUrl(){
        return "prod 환경 URL : " + appProperties.getUrl();
    }
}
