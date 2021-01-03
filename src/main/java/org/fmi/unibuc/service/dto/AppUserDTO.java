package org.fmi.unibuc.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A DTO for the {@link org.fmi.unibuc.domain.AppUser} entity.
 */
public class AppUserDTO implements Serializable {
    
    private Long id;


    private Long userId;
    private Set<TripDTO> trips = new HashSet<>();
    private Set<ExpenseDTO> expenses = new HashSet<>();
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Set<TripDTO> getTrips() {
        return trips;
    }

    public void setTrips(Set<TripDTO> trips) {
        this.trips = trips;
    }

    public Set<ExpenseDTO> getExpenses() {
        return expenses;
    }

    public void setExpenses(Set<ExpenseDTO> expenses) {
        this.expenses = expenses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppUserDTO)) {
            return false;
        }

        return id != null && id.equals(((AppUserDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppUserDTO{" +
            "id=" + getId() +
            ", userId=" + getUserId() +
            ", trips='" + getTrips() + "'" +
            ", expenses='" + getExpenses() + "'" +
            "}";
    }
}
