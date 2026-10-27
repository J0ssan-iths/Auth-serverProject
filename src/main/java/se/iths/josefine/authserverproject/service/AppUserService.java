package se.iths.josefine.authserverproject.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.iths.josefine.authserverproject.dto.AppUserRequestDTO;
import se.iths.josefine.authserverproject.dto.AppUserResponseDTO;
import se.iths.josefine.authserverproject.exception.UserNotFoundException;
import se.iths.josefine.authserverproject.mapper.AppUserMapper;
import se.iths.josefine.authserverproject.model.AppUser;
import se.iths.josefine.authserverproject.repository.UserRepository;

import java.util.List;

@Service
public class AppUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppUserMapper appUserMapper;

    public AppUserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AppUserMapper appUserMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.appUserMapper = appUserMapper;
    }

    /**
     * Metod för att hämta alla användare i en lista. Användarna hämtas som entitys
     * och mappas sedan om till dtos
     *
     * @return alla användare i en lista med användarnamn
     */
    public List<AppUserResponseDTO> findAll() {
        List<AppUser> appUsers = userRepository.findAll();
        return appUsers.stream().map(appUserMapper::toDto).toList();
    }

    /**
     * Hittar användare med id, användaren hämtas som en entity från databasen och mappas
     * om till en dto
     *
     * @param id används för att leta efter specifik användare
     * @return användaren som DTO
     * @throws UserNotFoundException om användaren inte finns
     */
    public AppUserResponseDTO findById(Long id) {
        AppUser appUser = getAppUser(id);

        AppUserResponseDTO appUserResponseDTO = appUserMapper.toDto(appUser);

        return appUserResponseDTO;
    }

    /**
     * Metod för att hämta specifik användare med ID
     * Om användaren inte hittas kastas ett UserNotFoundException
     *
     * @param id används för att hämta specifik användare
     * @return användare med efterfrågat id
     */
    public AppUser getAppUser(Long id) {
        AppUser appUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Användaren hittades inte"));
        return appUser;
    }

    /**
     * Metod för att skapa användare med Användarnamn, encoded lösenord och roll. Rollen sätts som standard
     * till ROLE_USER. Rollen kan sedan ändras i databasen.
     * Användaren omvandlas från en DTO till en entity för att sparas till databasen,
     * mappas tillbaka till en dto innan den returneras
     *
     * @param appUserRequestDTO som innehåller användarnamn och lösenord
     * @return en sparad användare som mappas från till en dto
     */
    public AppUserResponseDTO create(AppUserRequestDTO appUserRequestDTO) {
        AppUser appUser = appUserMapper.toEntity(appUserRequestDTO);
        appUser.setRole("USER");
        String encodedPassword = passwordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);
        AppUser savedUser = userRepository.save(appUser);
        return appUserMapper.toDto(savedUser);
    }
}
