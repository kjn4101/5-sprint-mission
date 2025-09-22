package com.codeit.rest.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final FileConfig fileConfig;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadPath = fileConfig.getUploadDir();
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:///" + uploadPath); // 윈도우도 리눅스도 모두 지원
    }
}
