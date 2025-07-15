package com.meguru.chatproject.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI chatProjectOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("chatproject 接口文档")
                        .description("chatproject接口文档")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("meguru")
                                .url("https://github.com/JustFunH/ChatProject")
                                .email("2258699133@qq.com")
                        )
                );
    }
}
