package se.iths.josefine.authserverproject.dto;


import java.util.List;

// DTO som bestämmer vilken information Auth Server ska skicka tillbaka
// till gatewayen, innehåller info om användaren som är inloggad just nu
public record AuthMeResponseDTO(
        String username,
        List<String> roles
) {
}
