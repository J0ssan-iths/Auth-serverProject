package se.iths.josefine.authserverproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import se.iths.josefine.authserverproject.dto.AppUserRequestDTO;
import se.iths.josefine.authserverproject.dto.AppUserResponseDTO;
import se.iths.josefine.authserverproject.service.AppUserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
/**
 * Endpoint för användare, för att nå endpoints i denna controller behövs /users efter urlen
 */
@RequestMapping("/users")
public class AppUserController {

    private final AppUserService appUserService;

    /**
     * Endpoint för att hämta alla användare, nås via /users/
     * Metoden anropar findAll i klassen appUserService
     */
    @GetMapping
    public List<AppUserResponseDTO> findAll() {
        return appUserService.findAll();
    }

    /**
     * Endpoint för att registrera ny användare, nås på //users/register
     *
     * @param appUserRequestDTO används till att efterfråga username och password vid ny användare
     * @return ny användare via metoden create i klassen appUserService
     */
    @PostMapping("/register")
    public AppUserResponseDTO create(@Valid @RequestBody AppUserRequestDTO appUserRequestDTO) {
        return appUserService.create(appUserRequestDTO);
    }
}
