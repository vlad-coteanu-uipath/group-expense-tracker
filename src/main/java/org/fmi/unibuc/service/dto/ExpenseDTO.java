package org.fmi.unibuc.service.dto;

import java.io.Serializable;
import org.fmi.unibuc.domain.enumeration.ExpenseType;

/**
 * A DTO for the {@link org.fmi.unibuc.domain.Expense} entity.
 */
public class ExpenseDTO implements Serializable {
    
    private Long id;

    private String description;

    private String amount;

    private ExpenseType type;


    private Long tripId;

    private Long createdById;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public ExpenseType getType() {
        return type;
    }

    public void setType(ExpenseType type) {
        this.type = type;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
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
        if (!(o instanceof ExpenseDTO)) {
            return false;
        }

        return id != null && id.equals(((ExpenseDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExpenseDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", amount='" + getAmount() + "'" +
            ", type='" + getType() + "'" +
            ", tripId=" + getTripId() +
            ", createdById=" + getCreatedById() +
            "}";
    }
}
