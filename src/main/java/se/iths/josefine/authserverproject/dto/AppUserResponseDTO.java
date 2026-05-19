package se.iths.josefine.authserverproject.dto;

import lombok.Getter;
import lombok.Setter;

public record AppUserResponseDTO(
        @Getter
        @Setter
        Long id,
        @Getter
        @Setter
        String username
) {
}
