package se.iths.josefine.authserverproject.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    //Med UrlBasedCorsConfigurationSource så kommer Spring Security automatisk konfigurera CORS.
    //Med följande kod kommer CORS support intregera med Spring Security
    @Bean
    UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        //Vi har 3 portar  tanke på att vi i gruppen kan köra sin frontend på olika Vite-portar
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:5173",
                "http://localhost:5174",
                "http://localhost:5175"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        //viktigt tanke på att vi skickar in cookies/Authorization-header
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
