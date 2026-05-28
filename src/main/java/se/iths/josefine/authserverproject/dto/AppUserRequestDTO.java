package se.iths.josefine.authserverproject.dto;

import jakarta.validation.constraints.Email;

public record AppUserRequestDTO(
        @Email(message = "Email måste vara giltig")
        String username,
        String password
) {
}
