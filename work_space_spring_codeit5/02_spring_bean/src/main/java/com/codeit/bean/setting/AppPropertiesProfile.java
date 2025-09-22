package com.codeit.bean.setting;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.server")
public class AppPropertiesProfile {
    private int port;
    private String url;
}
