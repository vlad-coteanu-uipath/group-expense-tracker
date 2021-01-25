package org.fmi.unibuc.service.dto;

import java.util.Set;

public class ExtendedTripDTO extends TripDTO {

    private Set<ExpenseDTO> tripExpenses;
    private Set<ExtendedUserDTO> tripParticipants;
    private ExtendedUserDTO extendedCreatedBy;

    public static ExtendedTripDTO fromTripDTO(TripDTO tripDTO) {
        ExtendedTripDTO extendedTripDTO = new ExtendedTripDTO();
        extendedTripDTO.setId(tripDTO.getId());
        extendedTripDTO.setCreatedById(tripDTO.getCreatedById());
        extendedTripDTO.setDescription(tripDTO.getDescription());
        extendedTripDTO.setCreatedDate(tripDTO.getCreatedDate());
        extendedTripDTO.setName(tripDTO.getName());
        return extendedTripDTO;
    }

    public Set<ExpenseDTO> getTripExpenses() {
        return tripExpenses;
    }

    public void setTripExpenses(Set<ExpenseDTO> tripExpenses) {
        this.tripExpenses = tripExpenses;
    }

    public Set<ExtendedUserDTO> getTripParticipants() {
        return tripParticipants;
    }

    public void setTripParticipants(Set<ExtendedUserDTO> tripParticipants) {
        this.tripParticipants = tripParticipants;
    }

    public ExtendedUserDTO getExtendedCreatedBy() {
        return extendedCreatedBy;
    }

    public void setExtendedCreatedBy(ExtendedUserDTO extendedCreatedBy) {
        this.extendedCreatedBy = extendedCreatedBy;
    }
}
