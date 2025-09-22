package com.codeit.aop.config;

import com.codeit.aop.filter.LoggingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<LoggingFilter> loggingFilter() {
        FilterRegistrationBean<LoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new LoggingFilter());
        registrationBean.addUrlPatterns("/api/*"); // 적용할 URL 패턴
        registrationBean.setOrder(1); // 필터 실행 순서 (낮을수록 먼저 실행)
        return registrationBean;
    }
}
