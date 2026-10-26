package se.iths.josefine.authserverproject.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Konfigurerar CORS för att tillåta kommunikation mellan backend och frontend
 */
@Configuration
public class CorsConfig {
    /**
     * Skapar enn CORS konfiguration som integreras med Spring Security
     * Tillåter anrop från frontend som körs på porterna 5173, 5174 och 5175 samt
     * specifierar tillåtna HTTP metoder och headers
     * Tillåter anrop med autentiseringsuppgifter som cookies
     *
     * @return UrlBasedConfigurationSource med registrerad Cors configuration
     * för olika sökvägar som används i applikationen
     */
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:5173",
                "http://localhost:5174",
                "http://localhost:5175"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
