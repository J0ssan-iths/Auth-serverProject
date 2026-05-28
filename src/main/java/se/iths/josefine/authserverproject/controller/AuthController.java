package se.iths.josefine.authserverproject.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.iths.josefine.authserverproject.dto.AppUserRequestDTO;
import se.iths.josefine.authserverproject.dto.TokenResponseDTO;
import se.iths.josefine.authserverproject.service.AuthService;

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
}
