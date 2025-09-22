package com.codeit.blog.scheduler;


import com.codeit.blog.repository.UserRepository;
import com.codeit.blog.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

//@Component
@RequiredArgsConstructor
public class MyScheduler {
    private final AuthService authService;
    private final UserRepository userRepository;

    // 5초마다 실행
    @Scheduled(fixedRate = 5000)
    public void task1(){
        System.out.println("5초 마다 실행 : " + LocalDateTime.now() );
//        userRepository.findAll().forEach(System.out::println);
    }

    // 이전 실행이 끝난 후 3초 뒤 실행
    @Scheduled(fixedDelay = 3000)
    public void task2(){
        System.out.println("종료 뒤 3초 마다 실행 : " + LocalDateTime.now() );
    }

    // 초기 10초 뒤 -> 5초 마다 반복
    @Scheduled(initialDelay = 10000, fixedRate = 5 * 1000)
    public void task3(){
        System.out.println("초기 10초 뒤 5초 마다 실행 : " + LocalDateTime.now() );
    }

    // 매일 오후 2시 30분 마다 실행
    // 초(0-59)   분(0-59)   시(0-23)   일(1-31)   월(1-12)   요일(0-7, 일=0 또는 7)
    @Scheduled(cron = "0 30 14 * * *")
    public void task4(){
        System.out.println("오후 2시 30분 : " + LocalDateTime.now() );
    }

}














