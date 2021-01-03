package org.fmi.unibuc.service.mapper;


import org.fmi.unibuc.domain.*;
import org.fmi.unibuc.service.dto.AppUserDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link AppUser} and its DTO {@link AppUserDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, TripMapper.class, ExpenseMapper.class})
public interface AppUserMapper extends EntityMapper<AppUserDTO, AppUser> {

    @Mapping(source = "user.id", target = "userId")
    AppUserDTO toDto(AppUser appUser);

    @Mapping(source = "userId", target = "user")
    @Mapping(target = "createdExpenses", ignore = true)
    @Mapping(target = "removeCreatedExpenses", ignore = true)
    @Mapping(target = "createdTrips", ignore = true)
    @Mapping(target = "removeCreatedTrips", ignore = true)
    @Mapping(target = "removeTrips", ignore = true)
    @Mapping(target = "removeExpenses", ignore = true)
    AppUser toEntity(AppUserDTO appUserDTO);

    default AppUser fromId(Long id) {
        if (id == null) {
            return null;
        }
        AppUser appUser = new AppUser();
        appUser.setId(id);
        return appUser;
    }
}
