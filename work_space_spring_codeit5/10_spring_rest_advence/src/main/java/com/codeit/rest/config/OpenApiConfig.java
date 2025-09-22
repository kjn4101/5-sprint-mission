package com.codeit.rest.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("REST API 문서")
                        .version("v1.1.0")
                        .description("Swagger REST API 문서입니다."))
                .servers(java.util.List.of(
                        new io.swagger.v3.oas.models.servers.Server()
                                .url("http://localhost:8080")
                                .description("로컬 서버1"),
                        new io.swagger.v3.oas.models.servers.Server()
                                .url("http://127.0.0.1:8080")
                                .description("로컬 서버2")
                ));
    }


    @Bean
    public GroupedOpenApi v1Api() {
        return GroupedOpenApi.builder()
                .group("v1")
                .pathsToMatch("/api/v1/**")
                .build();
    }

    @Bean
    public GroupedOpenApi v2Api() {
        return GroupedOpenApi.builder()
                .group("v2")
                .pathsToMatch("/api/v2/**")
                .build();
    }


}
