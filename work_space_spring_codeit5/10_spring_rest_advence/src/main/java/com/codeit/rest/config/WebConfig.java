package com.codeit.rest.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
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

   // cors 개발 모드 처리
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")           // 모든 경로에 대해
                .allowedOriginPatterns("*") // 도메인
                .allowedMethods("*")         // 모든 HTTP 메소드 허용
                .allowedHeaders("*")         // 모든 요청 헤더 허용
                .allowCredentials(true)      // 인증정보 포함
                .maxAge(3600);               // Preflight 캐시 1시간
    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/api/**")  // /api 경로에 대해서만
//                .allowedOrigins("http://localhost:3000", "https://example.com") // 허용 도메인
//                .allowedMethods("GET", "POST")  // GET, POST만 허용
//                .allowedHeaders("Content-Type", "Authorization") // 필요한 헤더만 허용
//                .allowCredentials(true)  // 인증정보 포함 허용
//                .maxAge(3600);           // Preflight 캐시 1시간
//    }
}
