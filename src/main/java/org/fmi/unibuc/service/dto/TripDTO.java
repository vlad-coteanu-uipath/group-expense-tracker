package org.fmi.unibuc.service.dto;

import java.time.LocalDate;
import java.io.Serializable;

/**
 * A DTO for the {@link org.fmi.unibuc.domain.Trip} entity.
 */
public class TripDTO implements Serializable {

    private Long id;

    private String name;

    private String description;

    private LocalDate createdDate;
    
    private Long createdById;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long appUserId) {
        this.createdById = appUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TripDTO)) {
            return false;
        }

        return id != null && id.equals(((TripDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TripDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", createdById=" + getCreatedById() +
            "}";
    }
}
