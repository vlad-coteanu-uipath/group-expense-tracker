package org.fmi.unibuc.service.dto;

import org.fmi.unibuc.domain.enumeration.ExpenseType;

import java.util.Set;

public class ExtendedExpenseDTO {

    private long expenseId;
    private String description;
    private ExpenseType expenseType;
    private Set<ExtendedUserDTO> expenseParticipants;
    private double amount;

    public ExtendedExpenseDTO() {
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(long expenseId) {
        this.expenseId = expenseId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ExpenseType getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(ExpenseType expenseType) {
        this.expenseType = expenseType;
    }

    public Set<ExtendedUserDTO> getExpenseParticipants() {
        return expenseParticipants;
    }

    public void setExpenseParticipants(Set<ExtendedUserDTO> expenseParticipants) {
        this.expenseParticipants = expenseParticipants;
    }
}
