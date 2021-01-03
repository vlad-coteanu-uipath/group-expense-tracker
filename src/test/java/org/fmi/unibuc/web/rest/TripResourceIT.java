package org.fmi.unibuc.web.rest;

import org.fmi.unibuc.GroupExpenseTrackerApp;
import org.fmi.unibuc.domain.Trip;
import org.fmi.unibuc.domain.Expense;
import org.fmi.unibuc.domain.AppUser;
import org.fmi.unibuc.repository.TripRepository;
import org.fmi.unibuc.service.TripService;
import org.fmi.unibuc.service.dto.TripDTO;
import org.fmi.unibuc.service.mapper.TripMapper;
import org.fmi.unibuc.service.dto.TripCriteria;
import org.fmi.unibuc.service.TripQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link TripResource} REST controller.
 */
@SpringBootTest(classes = GroupExpenseTrackerApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class TripResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_DATE = LocalDate.ofEpochDay(-1L);

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private TripMapper tripMapper;

    @Autowired
    private TripService tripService;

    @Autowired
    private TripQueryService tripQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTripMockMvc;

    private Trip trip;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trip createEntity(EntityManager em) {
        Trip trip = new Trip()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .createdDate(DEFAULT_CREATED_DATE);
        return trip;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trip createUpdatedEntity(EntityManager em) {
        Trip trip = new Trip()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdDate(UPDATED_CREATED_DATE);
        return trip;
    }

    @BeforeEach
    public void initTest() {
        trip = createEntity(em);
    }

    @Test
    @Transactional
    public void createTrip() throws Exception {
        int databaseSizeBeforeCreate = tripRepository.findAll().size();
        // Create the Trip
        TripDTO tripDTO = tripMapper.toDto(trip);
        restTripMockMvc.perform(post("/api/trips")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tripDTO)))
            .andExpect(status().isCreated());

        // Validate the Trip in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeCreate + 1);
        Trip testTrip = tripList.get(tripList.size() - 1);
        assertThat(testTrip.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTrip.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTrip.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    public void createTripWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tripRepository.findAll().size();

        // Create the Trip with an existing ID
        trip.setId(1L);
        TripDTO tripDTO = tripMapper.toDto(trip);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTripMockMvc.perform(post("/api/trips")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tripDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Trip in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTrips() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList
        restTripMockMvc.perform(get("/api/trips?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trip.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getTrip() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get the trip
        restTripMockMvc.perform(get("/api/trips/{id}", trip.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trip.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }


    @Test
    @Transactional
    public void getTripsByIdFiltering() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        Long id = trip.getId();

        defaultTripShouldBeFound("id.equals=" + id);
        defaultTripShouldNotBeFound("id.notEquals=" + id);

        defaultTripShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTripShouldNotBeFound("id.greaterThan=" + id);

        defaultTripShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTripShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllTripsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where name equals to DEFAULT_NAME
        defaultTripShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the tripList where name equals to UPDATED_NAME
        defaultTripShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTripsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where name not equals to DEFAULT_NAME
        defaultTripShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the tripList where name not equals to UPDATED_NAME
        defaultTripShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTripsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTripShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the tripList where name equals to UPDATED_NAME
        defaultTripShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTripsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where name is not null
        defaultTripShouldBeFound("name.specified=true");

        // Get all the tripList where name is null
        defaultTripShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllTripsByNameContainsSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where name contains DEFAULT_NAME
        defaultTripShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the tripList where name contains UPDATED_NAME
        defaultTripShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTripsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where name does not contain DEFAULT_NAME
        defaultTripShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the tripList where name does not contain UPDATED_NAME
        defaultTripShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllTripsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where description equals to DEFAULT_DESCRIPTION
        defaultTripShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the tripList where description equals to UPDATED_DESCRIPTION
        defaultTripShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllTripsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where description not equals to DEFAULT_DESCRIPTION
        defaultTripShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the tripList where description not equals to UPDATED_DESCRIPTION
        defaultTripShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllTripsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultTripShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the tripList where description equals to UPDATED_DESCRIPTION
        defaultTripShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllTripsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where description is not null
        defaultTripShouldBeFound("description.specified=true");

        // Get all the tripList where description is null
        defaultTripShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllTripsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where description contains DEFAULT_DESCRIPTION
        defaultTripShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the tripList where description contains UPDATED_DESCRIPTION
        defaultTripShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllTripsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where description does not contain DEFAULT_DESCRIPTION
        defaultTripShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the tripList where description does not contain UPDATED_DESCRIPTION
        defaultTripShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllTripsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where createdDate equals to DEFAULT_CREATED_DATE
        defaultTripShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the tripList where createdDate equals to UPDATED_CREATED_DATE
        defaultTripShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllTripsByCreatedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where createdDate not equals to DEFAULT_CREATED_DATE
        defaultTripShouldNotBeFound("createdDate.notEquals=" + DEFAULT_CREATED_DATE);

        // Get all the tripList where createdDate not equals to UPDATED_CREATED_DATE
        defaultTripShouldBeFound("createdDate.notEquals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllTripsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultTripShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the tripList where createdDate equals to UPDATED_CREATED_DATE
        defaultTripShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllTripsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where createdDate is not null
        defaultTripShouldBeFound("createdDate.specified=true");

        // Get all the tripList where createdDate is null
        defaultTripShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllTripsByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where createdDate is greater than or equal to DEFAULT_CREATED_DATE
        defaultTripShouldBeFound("createdDate.greaterThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the tripList where createdDate is greater than or equal to UPDATED_CREATED_DATE
        defaultTripShouldNotBeFound("createdDate.greaterThanOrEqual=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllTripsByCreatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where createdDate is less than or equal to DEFAULT_CREATED_DATE
        defaultTripShouldBeFound("createdDate.lessThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the tripList where createdDate is less than or equal to SMALLER_CREATED_DATE
        defaultTripShouldNotBeFound("createdDate.lessThanOrEqual=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllTripsByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where createdDate is less than DEFAULT_CREATED_DATE
        defaultTripShouldNotBeFound("createdDate.lessThan=" + DEFAULT_CREATED_DATE);

        // Get all the tripList where createdDate is less than UPDATED_CREATED_DATE
        defaultTripShouldBeFound("createdDate.lessThan=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllTripsByCreatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList where createdDate is greater than DEFAULT_CREATED_DATE
        defaultTripShouldNotBeFound("createdDate.greaterThan=" + DEFAULT_CREATED_DATE);

        // Get all the tripList where createdDate is greater than SMALLER_CREATED_DATE
        defaultTripShouldBeFound("createdDate.greaterThan=" + SMALLER_CREATED_DATE);
    }


    @Test
    @Transactional
    public void getAllTripsByExpensesIsEqualToSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);
        Expense expenses = ExpenseResourceIT.createEntity(em);
        em.persist(expenses);
        em.flush();
        trip.addExpenses(expenses);
        tripRepository.saveAndFlush(trip);
        Long expensesId = expenses.getId();

        // Get all the tripList where expenses equals to expensesId
        defaultTripShouldBeFound("expensesId.equals=" + expensesId);

        // Get all the tripList where expenses equals to expensesId + 1
        defaultTripShouldNotBeFound("expensesId.equals=" + (expensesId + 1));
    }


    @Test
    @Transactional
    public void getAllTripsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);
        AppUser createdBy = AppUserResourceIT.createEntity(em);
        em.persist(createdBy);
        em.flush();
        trip.setCreatedBy(createdBy);
        tripRepository.saveAndFlush(trip);
        Long createdById = createdBy.getId();

        // Get all the tripList where createdBy equals to createdById
        defaultTripShouldBeFound("createdById.equals=" + createdById);

        // Get all the tripList where createdBy equals to createdById + 1
        defaultTripShouldNotBeFound("createdById.equals=" + (createdById + 1));
    }


    @Test
    @Transactional
    public void getAllTripsByParticipantsIsEqualToSomething() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);
        AppUser participants = AppUserResourceIT.createEntity(em);
        em.persist(participants);
        em.flush();
        trip.addParticipants(participants);
        tripRepository.saveAndFlush(trip);
        Long participantsId = participants.getId();

        // Get all the tripList where participants equals to participantsId
        defaultTripShouldBeFound("participantsId.equals=" + participantsId);

        // Get all the tripList where participants equals to participantsId + 1
        defaultTripShouldNotBeFound("participantsId.equals=" + (participantsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTripShouldBeFound(String filter) throws Exception {
        restTripMockMvc.perform(get("/api/trips?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trip.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restTripMockMvc.perform(get("/api/trips/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTripShouldNotBeFound(String filter) throws Exception {
        restTripMockMvc.perform(get("/api/trips?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTripMockMvc.perform(get("/api/trips/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingTrip() throws Exception {
        // Get the trip
        restTripMockMvc.perform(get("/api/trips/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTrip() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        int databaseSizeBeforeUpdate = tripRepository.findAll().size();

        // Update the trip
        Trip updatedTrip = tripRepository.findById(trip.getId()).get();
        // Disconnect from session so that the updates on updatedTrip are not directly saved in db
        em.detach(updatedTrip);
        updatedTrip
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdDate(UPDATED_CREATED_DATE);
        TripDTO tripDTO = tripMapper.toDto(updatedTrip);

        restTripMockMvc.perform(put("/api/trips")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tripDTO)))
            .andExpect(status().isOk());

        // Validate the Trip in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeUpdate);
        Trip testTrip = tripList.get(tripList.size() - 1);
        assertThat(testTrip.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTrip.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTrip.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingTrip() throws Exception {
        int databaseSizeBeforeUpdate = tripRepository.findAll().size();

        // Create the Trip
        TripDTO tripDTO = tripMapper.toDto(trip);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTripMockMvc.perform(put("/api/trips")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tripDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Trip in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTrip() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        int databaseSizeBeforeDelete = tripRepository.findAll().size();

        // Delete the trip
        restTripMockMvc.perform(delete("/api/trips/{id}", trip.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
