package se.iths.josefine.authserverproject.component;

import org.springframework.stereotype.Component;
import se.iths.josefine.authserverproject.dto.AppUserRequestDTO;
import se.iths.josefine.authserverproject.dto.AppUserResponseDTO;
import se.iths.josefine.authserverproject.mapper.AppUserMapper;
import se.iths.josefine.authserverproject.model.AppUser;

@Component
public class AppUserComponent implements AppUserMapper {


    @Override
    public AppUser toEntity(AppUserRequestDTO appUserRequestDTO) {
        AppUser user = new AppUser();
        user.setUsername(appUserRequestDTO.username());
        user.setPassword(appUserRequestDTO.password());
        return user;
    }

    @Override
    public AppUserResponseDTO toDto(AppUser appUser) {
        AppUserResponseDTO userResponseDTO = new AppUserResponseDTO(
                appUser.getId(),
                appUser.getUsername()
        );
        return userResponseDTO;
    }

    @Override
    public void updateEntityFromDto(AppUserRequestDTO appUserRequestDTO, AppUser appUser) {
        appUser.setUsername(appUserRequestDTO.username());
        appUser.setPassword(appUserRequestDTO.password());
    }
}
