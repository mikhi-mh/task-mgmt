package org.mikhi.taskM.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Task API")
                .version("1.0")
                .description("API documentation for the Task Management application"));
    }
    // localhost:8080/v3/api-docs
    // localhost:8080/swagger-ui/index.html
}
