package com.codeit.aop.filter;

import jakarta.servlet.*;

import java.io.IOException;

public class LoggingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println(">> LoggingFilter 초기화");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("LoggingFilter >> 요청 들어옴: " + request.getRemoteAddr());
        chain.doFilter(request, response); // 다음 필터/서블릿으로 전달
        System.out.println("LoggingFilter << 응답 완료");
    }

    @Override
    public void destroy() {
        System.out.println("LoggingFilter >> LoggingFilter 종료");
    }
}
