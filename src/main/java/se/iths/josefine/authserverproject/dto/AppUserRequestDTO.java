package se.iths.josefine.authserverproject.dto;

import jakarta.validation.constraints.NotBlank;

public record AppUserRequestDTO(
        @NotBlank(message = "Email måste anges")
        String username,
        @NotBlank(message = "Lösenord måste anges")
        String password
) {
}
