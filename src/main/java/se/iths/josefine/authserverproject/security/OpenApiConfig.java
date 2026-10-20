package se.iths.josefine.authserverproject.security;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    // konfigurerar hur API:et visas och dokumenteras i Swagger
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // visar grundläggande information om Auth Servers API
                .info(new Info()
                        .title("Auth Server API")
                        .version("1.0.0")
                        .description("API för autentisering och användare"))

                // definierar JWT som en Bearer-token i HTTP-headern
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}
