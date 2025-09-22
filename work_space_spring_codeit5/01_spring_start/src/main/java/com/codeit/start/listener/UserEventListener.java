package com.codeit.start.listener;



import com.codeit.start.event.UserRegisteredEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

// UserRegisteredEvent 이벤트가 발생하면 이벤트를 받을 Listener
@Component // 해당 Class를 일반 Bean으로 등록하는 어노테이션
public class UserEventListener {

    @EventListener // 이벤트가 발생하면 실제 처리할 메서드 (call back 함수, handler, Listener)
    public void onApplicationEvent(UserRegisteredEvent event) {
        String username = event.getUser().getUsername();

        // 메일 전송하는 예시
        System.out.println("[메일 전송] " + username + "님이 가입하였습니다." );
        System.out.println("[메일 전송 완료]");
    }
}
