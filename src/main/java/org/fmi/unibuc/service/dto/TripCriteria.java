package org.fmi.unibuc.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link org.fmi.unibuc.domain.Trip} entity. This class is used
 * in {@link org.fmi.unibuc.web.rest.TripResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /trips?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TripCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private LocalDateFilter createdDate;

    private LongFilter expensesId;

    private LongFilter createdById;

    private LongFilter participantsId;

    public TripCriteria() {
    }

    public TripCriteria(TripCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.expensesId = other.expensesId == null ? null : other.expensesId.copy();
        this.createdById = other.createdById == null ? null : other.createdById.copy();
        this.participantsId = other.participantsId == null ? null : other.participantsId.copy();
    }

    @Override
    public TripCriteria copy() {
        return new TripCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LocalDateFilter getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateFilter createdDate) {
        this.createdDate = createdDate;
    }

    public LongFilter getExpensesId() {
        return expensesId;
    }

    public void setExpensesId(LongFilter expensesId) {
        this.expensesId = expensesId;
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
        final TripCriteria that = (TripCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(expensesId, that.expensesId) &&
            Objects.equals(createdById, that.createdById) &&
            Objects.equals(participantsId, that.participantsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        description,
        createdDate,
        expensesId,
        createdById,
        participantsId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TripCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
                (expensesId != null ? "expensesId=" + expensesId + ", " : "") +
                (createdById != null ? "createdById=" + createdById + ", " : "") +
                (participantsId != null ? "participantsId=" + participantsId + ", " : "") +
            "}";
    }

}
