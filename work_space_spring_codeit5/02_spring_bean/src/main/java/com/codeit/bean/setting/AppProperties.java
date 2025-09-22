package com.codeit.bean.setting;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "app")
@Validated
public class AppProperties {
    @NotBlank(message = "앱 이름은 반드시 설정해야 합니다.")
    private String name;

    private String version;

    @NotNull(message = "서버 설정은 필수입니다.")
    private Server server;

    private List<String> admins;

    private Map<String, String> metadata;

    @Data
    public static class Server {
        @Min(value = 1, message = "서버 포트는 1 이상이어야 합니다.")
        private int port;

        @NotBlank(message = "서버 URL은 반드시 설정해야 합니다.")
        private String url;

        @Min(value = 1000, message = "타임아웃은 최소 1000ms 이상이어야 합니다.")
        private int timeout;
    }
}
