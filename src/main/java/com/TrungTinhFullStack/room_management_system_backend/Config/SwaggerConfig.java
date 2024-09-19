package com.TrungTinhFullStack.room_management_system_backend.Config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;

public class SwaggerConfig {
    @Bean
    public GroupedOpenApi publicAPI() {

        return GroupedOpenApi.builder()
                .group("spring-public")
                .pathsToMatch("/api/**")
                .build();
    }
}
