package org.fmi.unibuc.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import org.fmi.unibuc.domain.enumeration.ExpenseType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;

/**
 * Criteria class for the {@link org.fmi.unibuc.domain.Expense} entity. This class is used
 * in {@link org.fmi.unibuc.web.rest.ExpenseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /expenses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ExpenseCriteria implements Serializable, Criteria {
    /**
     * Class for filtering ExpenseType
     */
    public static class ExpenseTypeFilter extends Filter<ExpenseType> {

        public ExpenseTypeFilter() {
        }

        public ExpenseTypeFilter(ExpenseTypeFilter filter) {
            super(filter);
        }

        @Override
        public ExpenseTypeFilter copy() {
            return new ExpenseTypeFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter description;

    private BigDecimalFilter amount;

    private ExpenseTypeFilter type;

    private LongFilter tripId;

    private LongFilter createdById;

    private LongFilter participantsId;

    public ExpenseCriteria() {
    }

    public ExpenseCriteria(ExpenseCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.amount = other.amount == null ? null : other.amount.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.tripId = other.tripId == null ? null : other.tripId.copy();
        this.createdById = other.createdById == null ? null : other.createdById.copy();
        this.participantsId = other.participantsId == null ? null : other.participantsId.copy();
    }

    @Override
    public ExpenseCriteria copy() {
        return new ExpenseCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public BigDecimalFilter getAmount() {
        return amount;
    }

    public void setAmount(BigDecimalFilter amount) {
        this.amount = amount;
    }

    public ExpenseTypeFilter getType() {
        return type;
    }

    public void setType(ExpenseTypeFilter type) {
        this.type = type;
    }

    public LongFilter getTripId() {
        return tripId;
    }

    public void setTripId(LongFilter tripId) {
        this.tripId = tripId;
    }

    public LongFilter getCreatedById() {
        return createdById;
    }

    public void setCreatedById(LongFilter createdById) {
        this.createdById = createdById;
    }

    public LongFilter getParticipantsId() {
        return participantsId;
    }

    public void setParticipantsId(LongFilter participantsId) {
        this.participantsId = participantsId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ExpenseCriteria that = (ExpenseCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(description, that.description) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(type, that.type) &&
            Objects.equals(tripId, that.tripId) &&
            Objects.equals(createdById, that.createdById) &&
            Objects.equals(participantsId, that.participantsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        description,
        amount,
        type,
        tripId,
        createdById,
        participantsId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExpenseCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (amount != null ? "amount=" + amount + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (tripId != null ? "tripId=" + tripId + ", " : "") +
                (createdById != null ? "createdById=" + createdById + ", " : "") +
                (participantsId != null ? "participantsId=" + participantsId + ", " : "") +
            "}";
    }

}
