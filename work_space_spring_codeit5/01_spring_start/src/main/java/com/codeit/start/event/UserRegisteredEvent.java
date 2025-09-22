package com.codeit.start.event;

import com.codeit.start.domain.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

// 사용자 등록된 경우 발생하는 이벤트
// ApplicationEvent : 이벤트를 생성할때 상속하는 Class
@Getter
public class UserRegisteredEvent extends ApplicationEvent {
    private final User user;

    public UserRegisteredEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}
