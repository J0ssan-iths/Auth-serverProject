package se.iths.josefine.authserverproject.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import se.iths.josefine.authserverproject.dto.AppUserRequestDTO;
import se.iths.josefine.authserverproject.dto.AuthMeResponseDTO;
import se.iths.josefine.authserverproject.dto.TokenResponseDTO;
import se.iths.josefine.authserverproject.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(
            @Valid @RequestBody AppUserRequestDTO appUserRequestDTO
    ) {
        return ResponseEntity.ok(authService.login(appUserRequestDTO));
    }

    @GetMapping("/jwks")
    public ResponseEntity<Map<String, Object>> publicJwks() {
        return ResponseEntity.ok(authService.publicJwkSet());
    }


    // returnerar information om användaren vars JWT-token har validerats
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

        // skickar tillbaka användarnamn och roller (som JSON), men inte själva token
        return ResponseEntity.ok(new AuthMeResponseDTO(username, roles));
    }


}
