package org.fmi.unibuc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Trip.
 */
@Entity
@Table(name = "trip")
public class Trip implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @OneToMany(mappedBy = "trip")
    private Set<Expense> expenses = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "createdTrips", allowSetters = true)
    private AppUser createdBy;

    @ManyToMany(mappedBy = "trips")
    @JsonIgnore
    private Set<AppUser> participants = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Trip name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Trip description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public Trip createdDate(LocalDate createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public Set<Expense> getExpenses() {
        return expenses;
    }

    public Trip expenses(Set<Expense> expenses) {
        this.expenses = expenses;
        return this;
    }

    public Trip addExpenses(Expense expense) {
        this.expenses.add(expense);
        expense.setTrip(this);
        return this;
    }

    public Trip removeExpenses(Expense expense) {
        this.expenses.remove(expense);
        expense.setTrip(null);
        return this;
    }

    public void setExpenses(Set<Expense> expenses) {
        this.expenses = expenses;
    }

    public AppUser getCreatedBy() {
        return createdBy;
    }

    public Trip createdBy(AppUser appUser) {
        this.createdBy = appUser;
        return this;
    }

    public void setCreatedBy(AppUser appUser) {
        this.createdBy = appUser;
    }

    public Set<AppUser> getParticipants() {
        return participants;
    }

    public Trip participants(Set<AppUser> appUsers) {
        this.participants = appUsers;
        return this;
    }

    public Trip addParticipants(AppUser appUser) {
        this.participants.add(appUser);
        appUser.getTrips().add(this);
        return this;
    }

    public Trip removeParticipants(AppUser appUser) {
        this.participants.remove(appUser);
        appUser.getTrips().remove(this);
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
        if (!(o instanceof Trip)) {
            return false;
        }
        return id != null && id.equals(((Trip) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Trip{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
