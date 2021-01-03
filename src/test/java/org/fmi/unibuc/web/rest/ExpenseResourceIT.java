package org.fmi.unibuc.web.rest;

import org.fmi.unibuc.GroupExpenseTrackerApp;
import org.fmi.unibuc.domain.Expense;
import org.fmi.unibuc.domain.Trip;
import org.fmi.unibuc.domain.AppUser;
import org.fmi.unibuc.repository.ExpenseRepository;
import org.fmi.unibuc.service.ExpenseService;
import org.fmi.unibuc.service.dto.ExpenseDTO;
import org.fmi.unibuc.service.mapper.ExpenseMapper;
import org.fmi.unibuc.service.dto.ExpenseCriteria;
import org.fmi.unibuc.service.ExpenseQueryService;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.fmi.unibuc.domain.enumeration.ExpenseType;
/**
 * Integration tests for the {@link ExpenseResource} REST controller.
 */
@SpringBootTest(classes = GroupExpenseTrackerApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ExpenseResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_AMOUNT = "AAAAAAAAAA";
    private static final String UPDATED_AMOUNT = "BBBBBBBBBB";

    private static final ExpenseType DEFAULT_TYPE = ExpenseType.INDIVIDUAL;
    private static final ExpenseType UPDATED_TYPE = ExpenseType.GROUP;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ExpenseMapper expenseMapper;

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private ExpenseQueryService expenseQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExpenseMockMvc;

    private Expense expense;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Expense createEntity(EntityManager em) {
        Expense expense = new Expense()
            .description(DEFAULT_DESCRIPTION)
            .amount(DEFAULT_AMOUNT)
            .type(DEFAULT_TYPE);
        return expense;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Expense createUpdatedEntity(EntityManager em) {
        Expense expense = new Expense()
            .description(UPDATED_DESCRIPTION)
            .amount(UPDATED_AMOUNT)
            .type(UPDATED_TYPE);
        return expense;
    }

    @BeforeEach
    public void initTest() {
        expense = createEntity(em);
    }

    @Test
    @Transactional
    public void createExpense() throws Exception {
        int databaseSizeBeforeCreate = expenseRepository.findAll().size();
        // Create the Expense
        ExpenseDTO expenseDTO = expenseMapper.toDto(expense);
        restExpenseMockMvc.perform(post("/api/expenses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(expenseDTO)))
            .andExpect(status().isCreated());

        // Validate the Expense in the database
        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeCreate + 1);
        Expense testExpense = expenseList.get(expenseList.size() - 1);
        assertThat(testExpense.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testExpense.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testExpense.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createExpenseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = expenseRepository.findAll().size();

        // Create the Expense with an existing ID
        expense.setId(1L);
        ExpenseDTO expenseDTO = expenseMapper.toDto(expense);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExpenseMockMvc.perform(post("/api/expenses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(expenseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Expense in the database
        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllExpenses() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList
        restExpenseMockMvc.perform(get("/api/expenses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expense.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getExpense() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get the expense
        restExpenseMockMvc.perform(get("/api/expenses/{id}", expense.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(expense.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }


    @Test
    @Transactional
    public void getExpensesByIdFiltering() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        Long id = expense.getId();

        defaultExpenseShouldBeFound("id.equals=" + id);
        defaultExpenseShouldNotBeFound("id.notEquals=" + id);

        defaultExpenseShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultExpenseShouldNotBeFound("id.greaterThan=" + id);

        defaultExpenseShouldBeFound("id.lessThanOrEqual=" + id);
        defaultExpenseShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllExpensesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where description equals to DEFAULT_DESCRIPTION
        defaultExpenseShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the expenseList where description equals to UPDATED_DESCRIPTION
        defaultExpenseShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllExpensesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where description not equals to DEFAULT_DESCRIPTION
        defaultExpenseShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the expenseList where description not equals to UPDATED_DESCRIPTION
        defaultExpenseShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllExpensesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultExpenseShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the expenseList where description equals to UPDATED_DESCRIPTION
        defaultExpenseShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllExpensesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where description is not null
        defaultExpenseShouldBeFound("description.specified=true");

        // Get all the expenseList where description is null
        defaultExpenseShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllExpensesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where description contains DEFAULT_DESCRIPTION
        defaultExpenseShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the expenseList where description contains UPDATED_DESCRIPTION
        defaultExpenseShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllExpensesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where description does not contain DEFAULT_DESCRIPTION
        defaultExpenseShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the expenseList where description does not contain UPDATED_DESCRIPTION
        defaultExpenseShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllExpensesByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where amount equals to DEFAULT_AMOUNT
        defaultExpenseShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the expenseList where amount equals to UPDATED_AMOUNT
        defaultExpenseShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllExpensesByAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where amount not equals to DEFAULT_AMOUNT
        defaultExpenseShouldNotBeFound("amount.notEquals=" + DEFAULT_AMOUNT);

        // Get all the expenseList where amount not equals to UPDATED_AMOUNT
        defaultExpenseShouldBeFound("amount.notEquals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllExpensesByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultExpenseShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the expenseList where amount equals to UPDATED_AMOUNT
        defaultExpenseShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllExpensesByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where amount is not null
        defaultExpenseShouldBeFound("amount.specified=true");

        // Get all the expenseList where amount is null
        defaultExpenseShouldNotBeFound("amount.specified=false");
    }
                @Test
    @Transactional
    public void getAllExpensesByAmountContainsSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where amount contains DEFAULT_AMOUNT
        defaultExpenseShouldBeFound("amount.contains=" + DEFAULT_AMOUNT);

        // Get all the expenseList where amount contains UPDATED_AMOUNT
        defaultExpenseShouldNotBeFound("amount.contains=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllExpensesByAmountNotContainsSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where amount does not contain DEFAULT_AMOUNT
        defaultExpenseShouldNotBeFound("amount.doesNotContain=" + DEFAULT_AMOUNT);

        // Get all the expenseList where amount does not contain UPDATED_AMOUNT
        defaultExpenseShouldBeFound("amount.doesNotContain=" + UPDATED_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllExpensesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where type equals to DEFAULT_TYPE
        defaultExpenseShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the expenseList where type equals to UPDATED_TYPE
        defaultExpenseShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllExpensesByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where type not equals to DEFAULT_TYPE
        defaultExpenseShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the expenseList where type not equals to UPDATED_TYPE
        defaultExpenseShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllExpensesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultExpenseShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the expenseList where type equals to UPDATED_TYPE
        defaultExpenseShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllExpensesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where type is not null
        defaultExpenseShouldBeFound("type.specified=true");

        // Get all the expenseList where type is null
        defaultExpenseShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllExpensesByTripIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);
        Trip trip = TripResourceIT.createEntity(em);
        em.persist(trip);
        em.flush();
        expense.setTrip(trip);
        expenseRepository.saveAndFlush(expense);
        Long tripId = trip.getId();

        // Get all the expenseList where trip equals to tripId
        defaultExpenseShouldBeFound("tripId.equals=" + tripId);

        // Get all the expenseList where trip equals to tripId + 1
        defaultExpenseShouldNotBeFound("tripId.equals=" + (tripId + 1));
    }


    @Test
    @Transactional
    public void getAllExpensesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);
        AppUser createdBy = AppUserResourceIT.createEntity(em);
        em.persist(createdBy);
        em.flush();
        expense.setCreatedBy(createdBy);
        expenseRepository.saveAndFlush(expense);
        Long createdById = createdBy.getId();

        // Get all the expenseList where createdBy equals to createdById
        defaultExpenseShouldBeFound("createdById.equals=" + createdById);

        // Get all the expenseList where createdBy equals to createdById + 1
        defaultExpenseShouldNotBeFound("createdById.equals=" + (createdById + 1));
    }


    @Test
    @Transactional
    public void getAllExpensesByParticipantsIsEqualToSomething() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);
        AppUser participants = AppUserResourceIT.createEntity(em);
        em.persist(participants);
        em.flush();
        expense.addParticipants(participants);
        expenseRepository.saveAndFlush(expense);
        Long participantsId = participants.getId();

        // Get all the expenseList where participants equals to participantsId
        defaultExpenseShouldBeFound("participantsId.equals=" + participantsId);

        // Get all the expenseList where participants equals to participantsId + 1
        defaultExpenseShouldNotBeFound("participantsId.equals=" + (participantsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExpenseShouldBeFound(String filter) throws Exception {
        restExpenseMockMvc.perform(get("/api/expenses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expense.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));

        // Check, that the count call also returns 1
        restExpenseMockMvc.perform(get("/api/expenses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExpenseShouldNotBeFound(String filter) throws Exception {
        restExpenseMockMvc.perform(get("/api/expenses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExpenseMockMvc.perform(get("/api/expenses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingExpense() throws Exception {
        // Get the expense
        restExpenseMockMvc.perform(get("/api/expenses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExpense() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        int databaseSizeBeforeUpdate = expenseRepository.findAll().size();

        // Update the expense
        Expense updatedExpense = expenseRepository.findById(expense.getId()).get();
        // Disconnect from session so that the updates on updatedExpense are not directly saved in db
        em.detach(updatedExpense);
        updatedExpense
            .description(UPDATED_DESCRIPTION)
            .amount(UPDATED_AMOUNT)
            .type(UPDATED_TYPE);
        ExpenseDTO expenseDTO = expenseMapper.toDto(updatedExpense);

        restExpenseMockMvc.perform(put("/api/expenses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(expenseDTO)))
            .andExpect(status().isOk());

        // Validate the Expense in the database
        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeUpdate);
        Expense testExpense = expenseList.get(expenseList.size() - 1);
        assertThat(testExpense.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testExpense.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testExpense.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingExpense() throws Exception {
        int databaseSizeBeforeUpdate = expenseRepository.findAll().size();

        // Create the Expense
        ExpenseDTO expenseDTO = expenseMapper.toDto(expense);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpenseMockMvc.perform(put("/api/expenses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(expenseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Expense in the database
        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteExpense() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        int databaseSizeBeforeDelete = expenseRepository.findAll().size();

        // Delete the expense
        restExpenseMockMvc.perform(delete("/api/expenses/{id}", expense.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
