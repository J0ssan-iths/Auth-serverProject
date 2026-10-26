package se.iths.josefine.authserverproject.security;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.StringUtils;
import se.iths.josefine.authserverproject.model.AppUser;
import se.iths.josefine.authserverproject.repository.UserRepository;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Denna klass konfigurerar applikationens säkerhet med Spring Security
 * Aktiverar webbsäkerhet och hur åtkomsten till applikationens endpoint ska hanteras
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final String jwtIssuer;
    private final String jwtPublicKey;
    private final String jwtPrivateKey;
    private final String jwtKeyId;

    public SecurityConfig(
            @Value("${app.jwt.issuer}") String jwtIssuer,
            @Value("${app.jwt.public-key}") String jwtPublicKey,
            @Value("${app.jwt.private-key}") String jwtPrivateKey,
            @Value("${app.jwt.key-id}") String jwtKeyId
    ) {
        this.jwtIssuer = jwtIssuer;
        this.jwtPublicKey = jwtPublicKey;
        this.jwtPrivateKey = jwtPrivateKey;
        this.jwtKeyId = jwtKeyId;
    }

    /**
     * Konfigurerar Spring Securitys FilterChain och bestämmer vilka endpoints som är
     * tillgängliga utan autentisering och vilka som kräver en inloggad användare
     * Metoden konfigurerar även CORS, CSRF och autentisering med JWT Bearer token
     *
     * @param http används för att konfigurera applikationens säkerhetsregler
     * @return en securityFilterChain med konfigurerade åtkomstregler och JWT autentisering
     * @throws Exception om ett fel uppstår vid konfiguration av kedjan
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
            throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login", "/auth/jwks").permitAll()
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/register").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().authenticated())
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                // gör att Spring Security autentiserar inkommande Bearer-token med JwtDecoder
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }

    /**
     * Skapar ett RSA nyckelpar som används för signering och verifiering av JWT token
     * Metoden läser in de privata och publika nycklarna från Base64 kodade värden
     * och omvandlar de till nycklar som kan användas av Java
     *
     * @return ett nyckelpar som innehåller den publika och privata RSA nyckeln
     * @throws Exception om fel uppstår när nyckelparet skapas
     */
    @Bean
    public KeyPair keyPair() throws Exception {
        if (StringUtils.hasText(jwtPrivateKey) && StringUtils.hasText(jwtPublicKey)) {
            byte[] privateBytes = Base64.getDecoder().decode(jwtPrivateKey);
            byte[] publicBytes = Base64.getDecoder().decode(jwtPublicKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateBytes));
            PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicBytes));
            return new KeyPair(publicKey, privateKey);
        }
        throw new IllegalArgumentException("Privat eller publik nyckel för JWT saknas");
    }

    /**
     * Skapar en JWKS som tillhandahåller RSA nyckelparet som sedan används för att signera
     * JWT token
     *
     * @param keyPair innehåller den publika och privata RSA nyckeln
     * @return en jwks som hanterar RSA nyckeln för JWT hantering
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource(KeyPair keyPair) {
        RSAKey rsaKey = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey((RSAPrivateKey) keyPair.getPrivate())
                .keyID(jwtKeyId)
                .build();
        return new ImmutableJWKSet<>(new JWKSet(rsaKey));
    }

    /**
     * Skapar en JwtEncoder som sedan används för att signera och koda JWT tokens
     * med hjälp av en konfigurerad källa för jwks
     *
     * @param jwkSource hanterar nycklar för signering
     * @return nimbusJwtEncoder som används för att skapa JWT token
     */
    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    /**
     * Metod som hämtar Spring Securitys AuthenticationManager för att kunna hantera
     * autentisering av användare
     *
     * @param authenticationConfiguration konfiguration som används för att hämta AuthenticationManager
     * @return AuthenticationManager som sedan används för att autentisera användare
     * @throws Exception om fel uppstår vid hämtning av AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Metod som skapar en PasswordEncoder för att hasha lösenord
     *
     * @return ett hashat lösenord innan den sparas i databasen
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Metod som skapar en UserDetailsService som hämtar användarens uppgifter från databasen
     *
     * @param userRepository används för att söka efter användare via användarnamnet
     * @return en userDetailsService som hanterar användarens uppgifter för
     * Spring Securitys autentisering
     */
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> {
            AppUser appUser = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User: " + username + " not found"));

            return User.builder()
                    .username(appUser.getUsername())
                    .password(appUser.getPassword())
                    .roles(appUser.getRole())
                    .build();
        };
    }

    /**
     * Skapar en decoder som kontrollerar JWT-token som skickas till Auth Server
     *
     * @param keyPair Spring hämtar den befintliga KeyPair-beanen och skickar in den som parameter
     * @return returnerar decodern så att Spring Security kan använda den för inkommande JWT-token
     */
    @Bean
    public JwtDecoder jwtDecoder(KeyPair keyPair) {
        // hämtar den publika nyckeln från nyckelparet
        // den publika nyckeln används för att kontrollera att token är korrekt signerad
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        // skapar en decoder som läser och verifierar JWT-token med den publika nyckeln
        NimbusJwtDecoder decoder = NimbusJwtDecoder
                .withPublicKey(publicKey)
                .build();

        // validerar både tokens giltighetstid och att rätt issuer har utfärdat den
        OAuth2TokenValidator<Jwt> validator =
                JwtValidators.createDefaultWithIssuer(jwtIssuer);

        // kopplar valideringen till decodern så att kontrollerna utförs vid validering
        decoder.setJwtValidator(validator);

        return decoder;
    }
}