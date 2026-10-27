package se.iths.josefine.authserverproject.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import se.iths.josefine.authserverproject.dto.AppUserRequestDTO;
import se.iths.josefine.authserverproject.dto.AuthMeResponseDTO;
import se.iths.josefine.authserverproject.dto.TokenResponseDTO;
import se.iths.josefine.authserverproject.service.AuthService;

import java.util.List;
import java.util.Map;

/**
 * Endpoint för användare, för att nå endpoints i denna controller behövs /auth efter urlen
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint för att logga in, /auth/login
     *
     * @param appUserRequestDTO används för att ha krav på username och password vid inloggning
     * @return en TokenResponseDTO med åtkomsttoken, giltighetstid, användarnamn och roller
     */
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(
            @Valid @RequestBody AppUserRequestDTO appUserRequestDTO
    ) {
        return ResponseEntity.ok(authService.login(appUserRequestDTO));
    }

    /**
     * Endpoint som returnerar den publika nyckeln (JWKS) som andra tjänster
     * använder för att verifiera JWT, /auth/jwks
     *
     * @return JWKS i JSON-format
     */
    @GetMapping("/jwks")
    public ResponseEntity<Map<String, Object>> publicJwks() {
        //returnerar en jwks nyckel
        return ResponseEntity.ok(authService.publicJwkSet());
    }


    /**
     * Endpoint som används för att återställa användare efter omladdning av sidan /auth/me
     * Returnerar information om användaren vars JWT-token har validerats
     *
     * @param jwt den validerade JWT-token för den inloggade användaren
     * @return skickar tillbaka användarnamn och roller (som JSON), men inte själva token
     */
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/me")
    public ResponseEntity<AuthMeResponseDTO> me(@AuthenticationPrincipal Jwt jwt) {

        // hämtar användarnamnet från tokens subject (sub)
        String username = jwt.getSubject();

        // hämtar rollerna från tokens roles-claim
        List<String> roles = jwt.getClaimAsStringList("roles");

        // en giltig token ska alltid innehålla minst en roll (USER eller ADMIN)
        if (roles == null || roles.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Token saknar roller"
            );
        }

        return ResponseEntity.ok(new AuthMeResponseDTO(username, roles));
    }
}
