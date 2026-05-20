package se.iths.josefine.authserverproject.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.iths.josefine.authserverproject.component.AppUserComponent;
import se.iths.josefine.authserverproject.dto.AppUserRequestDTO;
import se.iths.josefine.authserverproject.dto.AppUserResponseDTO;
import se.iths.josefine.authserverproject.exception.UserNotFoundException;
import se.iths.josefine.authserverproject.model.AppUser;
import se.iths.josefine.authserverproject.repository.UserRepository;

import java.util.List;

@Service
public class AppUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppUserComponent appUserComponent;

    public AppUserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AppUserComponent appUserComponent) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.appUserComponent = appUserComponent;
    }

    public List<AppUserResponseDTO> findAll() {
        List<AppUser> appUsers = userRepository.findAll();
        return appUsers.stream().map(appUserComponent::toDto).toList();
    }

    public AppUserResponseDTO findById(Long id) {
        AppUser appUser = getAppUser(id);

        AppUserResponseDTO appUserResponseDTO = appUserComponent.toDto(appUser);

        return appUserResponseDTO;
    }

    public AppUser getAppUser(Long id) {
        AppUser appUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Användaren hittades inte"));
        return appUser;
    }

    public AppUserResponseDTO create(AppUserRequestDTO appUserRequestDTO) {
        AppUser appUser = appUserComponent.toEntity(appUserRequestDTO);
        appUser.setRole("USER");
        String encodedPassword = passwordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);
        AppUser savedUser = userRepository.save(appUser);
        return appUserComponent.toDto(savedUser);
    }

    public void delete(Long id) {
        AppUser appUser = getAppUser(id);
        userRepository.delete(appUser);
    }
}
