package com.codeit.blog.actuator;


import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// 시스템의 로그인 횟수 및 실패 횟수를 카운팅하는 Metric
@Component
@RequiredArgsConstructor
@Slf4j
public class AuthMetrics {
    // 사용량 측정을 도와줄 객체
    private final MeterRegistry meterRegistry;

    // 카운터
    private Counter loginAttemptCounter; // 전체 시도
    private Counter loginSuccessCounter; // 성공 합계

    // 활성화 유저수를 카운트 할 map
    private final Map<String, Long> activeUserMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init(){
        loginAttemptCounter = Counter.builder("auth.login.attempts")
                .description("로그인 시도 횟수")
                .tag("system", "blog")
                .register(meterRegistry);

        loginSuccessCounter = Counter.builder("auth.login.success")
                .description("로그인 성공 횟수")
                .tag("system", "blog")
                .register(meterRegistry);

        // 현재 로그인 사용자 수 게이지
        Gauge.builder("auth.active.user.count", activeUserMap, Map::size)
                .description("현재 로그인 사용자 수")
                .tag("system", "blog")
                .register(meterRegistry);

    }

    public void incrementLoginAttempt(){
        loginAttemptCounter.increment();
    }

    public void onLoginSuccess(String username){
        loginSuccessCounter.increment();
        activeUserMap.put(username, 1L);
    }

    public void removeActiveUser(String username){
        activeUserMap.remove(username);
    }

    public int currentUserCount(){
        return activeUserMap.size();
    }

}
















