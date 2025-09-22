package com.codeit.di;

import com.codeit.di.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        // main에서 Context 선언하고 등록된 bean 가져오는 방법
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        UserService adminService = (UserService) context.getBean("adminService");
        UserService service = context.getBean(UserService.class);

        System.out.println(adminService.getClass().getName());
        System.out.println(service.getClass().getName());

        service.getAllUsers().forEach(System.out::println);

        System.out.println("localhost : http://localhost:8080/");
    }

}
