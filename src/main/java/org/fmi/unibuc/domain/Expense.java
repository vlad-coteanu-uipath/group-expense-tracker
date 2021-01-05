package org.fmi.unibuc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.fmi.unibuc.domain.enumeration.ExpenseType;

/**
 * A Expense.
 */
@Entity
@Table(name = "expense")
public class Expense implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "amount", precision = 21, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ExpenseType type;

    @ManyToOne
    @JsonIgnoreProperties(value = "expenses", allowSetters = true)
    private Trip trip;

    @ManyToOne
    @JsonIgnoreProperties(value = "createdExpenses", allowSetters = true)
    private AppUser createdBy;

    @ManyToMany(mappedBy = "expenses")
    @JsonIgnore
    private Set<AppUser> participants = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public Expense description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Expense amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public ExpenseType getType() {
        return type;
    }

    public Expense type(ExpenseType type) {
        this.type = type;
        return this;
    }

    public void setType(ExpenseType type) {
        this.type = type;
    }

    public Trip getTrip() {
        return trip;
    }

    public Expense trip(Trip trip) {
        this.trip = trip;
        return this;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public AppUser getCreatedBy() {
        return createdBy;
    }

    public Expense createdBy(AppUser appUser) {
        this.createdBy = appUser;
        return this;
    }

    public void setCreatedBy(AppUser appUser) {
        this.createdBy = appUser;
    }

    public Set<AppUser> getParticipants() {
        return participants;
    }

    public Expense participants(Set<AppUser> appUsers) {
        this.participants = appUsers;
        return this;
    }

    public Expense addParticipants(AppUser appUser) {
        this.participants.add(appUser);
        appUser.getExpenses().add(this);
        return this;
    }

    public Expense removeParticipants(AppUser appUser) {
        this.participants.remove(appUser);
        appUser.getExpenses().remove(this);
        return this;
    }

    public void setParticipants(Set<AppUser> appUsers) {
        this.participants = appUsers;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Expense)) {
            return false;
        }
        return id != null && id.equals(((Expense) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Expense{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", amount=" + getAmount() +
            ", type='" + getType() + "'" +
            "}";
    }
}
