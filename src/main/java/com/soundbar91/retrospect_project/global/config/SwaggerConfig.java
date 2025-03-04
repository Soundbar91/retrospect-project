package com.soundbar91.retrospect_project.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .components(new Components())
            .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
            .title("회고 프로젝트 API")
            .description("회고 프로젝트에서 구현된 API를 사용할 수 있습니다.")
            .version("1.0.0");
    }
}
