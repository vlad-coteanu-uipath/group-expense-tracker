package org.fmi.unibuc.service.mapper;


import org.fmi.unibuc.domain.*;
import org.fmi.unibuc.service.dto.TripDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Trip} and its DTO {@link TripDTO}.
 */
@Mapper(componentModel = "spring", uses = {AppUserMapper.class})
public interface TripMapper extends EntityMapper<TripDTO, Trip> {

    @Mapping(source = "createdBy.id", target = "createdById")
    TripDTO toDto(Trip trip);

    @Mapping(target = "expenses", ignore = true)
    @Mapping(target = "removeExpenses", ignore = true)
    @Mapping(source = "createdById", target = "createdBy")
    @Mapping(target = "participants", ignore = true)
    @Mapping(target = "removeParticipants", ignore = true)
    Trip toEntity(TripDTO tripDTO);

    default Trip fromId(Long id) {
        if (id == null) {
            return null;
        }
        Trip trip = new Trip();
        trip.setId(id);
        return trip;
    }
}
