package com.codeit.bean.setting;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
@Profile("dev") // dev 환경일때만 아래 DevConfig이 Bean으로 등록됨
public class DevConfig {

    private final AppPropertiesProfile appProperties;

    @Bean
    public String serverUrl(){
        return "DEV 환경 URL : " + appProperties.getUrl();
    }
}
