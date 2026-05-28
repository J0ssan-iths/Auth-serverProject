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
@RequestMapping("/users")
public class AppUserController {

    private final AppUserService appUserService;

    @GetMapping
    //ger null på username
    public List<AppUserResponseDTO> findAll() {
        return appUserService.findAll();
    }

    @PostMapping("/register")
    public AppUserResponseDTO create(@Valid @RequestBody AppUserRequestDTO appUserRequestDTO) {
        return appUserService.create(appUserRequestDTO);
    }
}
