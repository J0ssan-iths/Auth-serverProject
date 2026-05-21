package se.iths.josefine.authserverproject.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        String shemeName = "bearerAuth";
        return new OpenAPI()
                .info(new Info()
                        .title("Person Service API")
                        .description("Simple JWT protected person microservices")
                        .version("1.0"))
                .addSecurityItem(new SecurityRequirement().addList(shemeName))
                .components(new Components()
                        .addSecuritySchemes(shemeName, new SecurityScheme()
                                .name(shemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
