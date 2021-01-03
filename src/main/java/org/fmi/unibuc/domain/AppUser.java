package org.fmi.unibuc.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A AppUser.
 */
@Entity
@Table(name = "app_user")
public class AppUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "createdBy")
    private Set<Expense> createdExpenses = new HashSet<>();

    @OneToMany(mappedBy = "createdBy")
    private Set<Trip> createdTrips = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "app_user_trips",
               joinColumns = @JoinColumn(name = "app_user_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "trips_id", referencedColumnName = "id"))
    private Set<Trip> trips = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "app_user_expenses",
               joinColumns = @JoinColumn(name = "app_user_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "expenses_id", referencedColumnName = "id"))
    private Set<Expense> expenses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public AppUser user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Expense> getCreatedExpenses() {
        return createdExpenses;
    }

    public AppUser createdExpenses(Set<Expense> expenses) {
        this.createdExpenses = expenses;
        return this;
    }

    public AppUser addCreatedExpenses(Expense expense) {
        this.createdExpenses.add(expense);
        expense.setCreatedBy(this);
        return this;
    }

    public AppUser removeCreatedExpenses(Expense expense) {
        this.createdExpenses.remove(expense);
        expense.setCreatedBy(null);
        return this;
    }

    public void setCreatedExpenses(Set<Expense> expenses) {
        this.createdExpenses = expenses;
    }

    public Set<Trip> getCreatedTrips() {
        return createdTrips;
    }

    public AppUser createdTrips(Set<Trip> trips) {
        this.createdTrips = trips;
        return this;
    }

    public AppUser addCreatedTrips(Trip trip) {
        this.createdTrips.add(trip);
        trip.setCreatedBy(this);
        return this;
    }

    public AppUser removeCreatedTrips(Trip trip) {
        this.createdTrips.remove(trip);
        trip.setCreatedBy(null);
        return this;
    }

    public void setCreatedTrips(Set<Trip> trips) {
        this.createdTrips = trips;
    }

    public Set<Trip> getTrips() {
        return trips;
    }

    public AppUser trips(Set<Trip> trips) {
        this.trips = trips;
        return this;
    }

    public AppUser addTrips(Trip trip) {
        this.trips.add(trip);
        trip.getParticipants().add(this);
        return this;
    }

    public AppUser removeTrips(Trip trip) {
        this.trips.remove(trip);
        trip.getParticipants().remove(this);
        return this;
    }

    public void setTrips(Set<Trip> trips) {
        this.trips = trips;
    }

    public Set<Expense> getExpenses() {
        return expenses;
    }

    public AppUser expenses(Set<Expense> expenses) {
        this.expenses = expenses;
        return this;
    }

    public AppUser addExpenses(Expense expense) {
        this.expenses.add(expense);
        expense.getParticipants().add(this);
        return this;
    }

    public AppUser removeExpenses(Expense expense) {
        this.expenses.remove(expense);
        expense.getParticipants().remove(this);
        return this;
    }

    public void setExpenses(Set<Expense> expenses) {
        this.expenses = expenses;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppUser)) {
            return false;
        }
        return id != null && id.equals(((AppUser) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppUser{" +
            "id=" + getId() +
            "}";
    }
}
