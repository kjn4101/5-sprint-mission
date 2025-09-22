package com.codeit.aop.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoggingInterceptor implements HandlerInterceptor {

    // 컨트롤러 실행 전
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("LoggingInterceptor >> 요청 URI: " + request.getRequestURI());
        System.out.println(request.getRemoteAddr());
        return true; // true: 진행, false: 요청 차단
    }

    // 컨트롤러 실행 후 (뷰 렌더링 전)
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           org.springframework.web.servlet.ModelAndView modelAndView) throws Exception {
        System.out.println("LoggingInterceptor >> 컨트롤러 실행 후 처리");
    }

    // 뷰 렌더링까지 완료된 후 (예외도 포함)
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("LoggingInterceptor >> 요청 완료 후 처리");
    }
}
