package se.iths.josefine.authserverproject.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AppUserRequestDTO(
        @Email(message = "Email måste vara giltig")
        @NotBlank(message = "Email måste anges")
        String username,
        @NotBlank(message = "Lösenord måste anges")
        String password
) {
}
