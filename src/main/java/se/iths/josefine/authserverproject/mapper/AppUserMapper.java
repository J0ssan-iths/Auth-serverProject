package se.iths.josefine.authserverproject.mapper;

import org.mapstruct.Mapper;
import se.iths.josefine.authserverproject.dto.AppUserRequestDTO;
import se.iths.josefine.authserverproject.dto.AppUserResponseDTO;
import se.iths.josefine.authserverproject.model.AppUser;

@Mapper(componentModel = "spring")
public interface AppUserMapper {

    AppUser toEntity(AppUserRequestDTO appUserRequestDTO);

    AppUserResponseDTO toDto(AppUser appUser);

}
