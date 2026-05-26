package se.iths.josefine.authserverproject.controller;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import se.iths.josefine.authserverproject.dto.AppUserRequestDTO;
import se.iths.josefine.authserverproject.dto.AppUserResponseDTO;
import se.iths.josefine.authserverproject.service.AppUserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class AppUserController {

    private final AppUserService appUserService;

    
    @PostMapping
    public AppUserResponseDTO create(@Valid @RequestBody AppUserRequestDTO appUserRequestDTO) {
        return appUserService.create(appUserRequestDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        appUserService.delete(id);
    }
}
