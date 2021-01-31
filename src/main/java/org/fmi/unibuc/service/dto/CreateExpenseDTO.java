package org.fmi.unibuc.service.dto;

import org.fmi.unibuc.domain.enumeration.ExpenseType;

public class CreateExpenseDTO {

    private String description;
    private Long[] participantsAppUserId;
    private Long createdBy;
    private Long tripId;
    private double amount;
    private ExpenseType expenseType;

    public CreateExpenseDTO() {
    }

    public CreateExpenseDTO(String description, Long[] participantsAppUserId, Long createdBy, Long tripId, double amount, ExpenseType expenseType) {
        this.description = description;
        this.participantsAppUserId = participantsAppUserId;
        this.createdBy = createdBy;
        this.tripId = tripId;
        this.amount = amount;
        this.expenseType = expenseType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long[] getParticipantsAppUserId() {
        return participantsAppUserId;
    }

    public void setParticipantsAppUserId(Long[] participantsAppUserId) {
        this.participantsAppUserId = participantsAppUserId;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public ExpenseType getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(ExpenseType expenseType) {
        this.expenseType = expenseType;
    }
}
