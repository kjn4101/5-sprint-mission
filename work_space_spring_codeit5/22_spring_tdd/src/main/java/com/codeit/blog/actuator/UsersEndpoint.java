package com.codeit.blog.actuator;


import com.codeit.blog.repository.UserRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import javax.swing.text.html.parser.Entity;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;


@Component
@Endpoint(id = "users") // 커스텀 메트릭 추가하는 방법
@RequiredArgsConstructor
public class UsersEndpoint {
    private final MeterRegistry meterRegistry;
    private final UserRepository userRepository;

    @ReadOperation
    public Map<String, Object> users() {
        double attempts = getCounterValue("auth.login.attempts");
        double success  = getCounterValue("auth.login.success");

        return Map.of(
            "loginAttempts", attempts,
            "loginSuccess", success,
            "loginFail", attempts - success,
            "currentUsers", meterRegistry.find("auth.active.user.count").gauge().value(),
            "totalUsers", userRepository.count()
        );
    }

    private double getCounterValue(String name) {
        Counter c = meterRegistry.find(name).counter();
        return (c == null) ? 0.0 : c.count();
    }

}
