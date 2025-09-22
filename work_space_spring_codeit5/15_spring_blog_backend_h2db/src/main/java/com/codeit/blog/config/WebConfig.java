package com.codeit.blog.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")           // 모든 경로에 대해
                .allowedOriginPatterns("*")
                .allowedMethods("*")         // 모든 HTTP 메소드 허용
                .allowedHeaders("*")         // 모든 요청 헤더 허용
                .allowCredentials(true)      // 인증정보 포함
                .maxAge(3600);               // Preflight 캐시 1시간
    }

    private final FileConfig fileConfig;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // avatars
        String avatarDir = fileConfig.getAvatarUploadDirFile().getAbsolutePath() + File.separator;
        registry.addResourceHandler("/avatars/**")
                .addResourceLocations("file:///" + avatarDir);

        // attachments
        String attachDir = fileConfig.getAttachFileUploadDirFile().getAbsolutePath() + File.separator;
        registry.addResourceHandler("/attachments/**")
                .addResourceLocations("file:///" + attachDir);
    }

}
