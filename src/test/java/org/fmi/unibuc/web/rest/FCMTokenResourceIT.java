package org.fmi.unibuc.web.rest;

import org.fmi.unibuc.GroupExpenseTrackerApp;
import org.fmi.unibuc.domain.FCMToken;
import org.fmi.unibuc.repository.FCMTokenRepository;
import org.fmi.unibuc.service.FCMTokenService;

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

/**
 * Integration tests for the {@link FCMTokenResource} REST controller.
 */
@SpringBootTest(classes = GroupExpenseTrackerApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class FCMTokenResourceIT {

    private static final String DEFAULT_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_TOKEN = "BBBBBBBBBB";

    private static final Long DEFAULT_APP_USER_ID = 1L;
    private static final Long UPDATED_APP_USER_ID = 2L;

    @Autowired
    private FCMTokenRepository fCMTokenRepository;

    @Autowired
    private FCMTokenService fCMTokenService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFCMTokenMockMvc;

    private FCMToken fCMToken;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FCMToken createEntity(EntityManager em) {
        FCMToken fCMToken = new FCMToken()
            .token(DEFAULT_TOKEN)
            .appUserId(DEFAULT_APP_USER_ID);
        return fCMToken;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FCMToken createUpdatedEntity(EntityManager em) {
        FCMToken fCMToken = new FCMToken()
            .token(UPDATED_TOKEN)
            .appUserId(UPDATED_APP_USER_ID);
        return fCMToken;
    }

    @BeforeEach
    public void initTest() {
        fCMToken = createEntity(em);
    }

    @Test
    @Transactional
    public void createFCMToken() throws Exception {
        int databaseSizeBeforeCreate = fCMTokenRepository.findAll().size();
        // Create the FCMToken
        restFCMTokenMockMvc.perform(post("/api/fcm-tokens")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fCMToken)))
            .andExpect(status().isCreated());

        // Validate the FCMToken in the database
        List<FCMToken> fCMTokenList = fCMTokenRepository.findAll();
        assertThat(fCMTokenList).hasSize(databaseSizeBeforeCreate + 1);
        FCMToken testFCMToken = fCMTokenList.get(fCMTokenList.size() - 1);
        assertThat(testFCMToken.getToken()).isEqualTo(DEFAULT_TOKEN);
        assertThat(testFCMToken.getAppUserId()).isEqualTo(DEFAULT_APP_USER_ID);
    }

    @Test
    @Transactional
    public void createFCMTokenWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fCMTokenRepository.findAll().size();

        // Create the FCMToken with an existing ID
        fCMToken.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFCMTokenMockMvc.perform(post("/api/fcm-tokens")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fCMToken)))
            .andExpect(status().isBadRequest());

        // Validate the FCMToken in the database
        List<FCMToken> fCMTokenList = fCMTokenRepository.findAll();
        assertThat(fCMTokenList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllFCMTokens() throws Exception {
        // Initialize the database
        fCMTokenRepository.saveAndFlush(fCMToken);

        // Get all the fCMTokenList
        restFCMTokenMockMvc.perform(get("/api/fcm-tokens?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fCMToken.getId().intValue())))
            .andExpect(jsonPath("$.[*].token").value(hasItem(DEFAULT_TOKEN)))
            .andExpect(jsonPath("$.[*].appUserId").value(hasItem(DEFAULT_APP_USER_ID.intValue())));
    }
    
    @Test
    @Transactional
    public void getFCMToken() throws Exception {
        // Initialize the database
        fCMTokenRepository.saveAndFlush(fCMToken);

        // Get the fCMToken
        restFCMTokenMockMvc.perform(get("/api/fcm-tokens/{id}", fCMToken.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fCMToken.getId().intValue()))
            .andExpect(jsonPath("$.token").value(DEFAULT_TOKEN))
            .andExpect(jsonPath("$.appUserId").value(DEFAULT_APP_USER_ID.intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingFCMToken() throws Exception {
        // Get the fCMToken
        restFCMTokenMockMvc.perform(get("/api/fcm-tokens/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFCMToken() throws Exception {
        // Initialize the database
        fCMTokenService.save(fCMToken);

        int databaseSizeBeforeUpdate = fCMTokenRepository.findAll().size();

        // Update the fCMToken
        FCMToken updatedFCMToken = fCMTokenRepository.findById(fCMToken.getId()).get();
        // Disconnect from session so that the updates on updatedFCMToken are not directly saved in db
        em.detach(updatedFCMToken);
        updatedFCMToken
            .token(UPDATED_TOKEN)
            .appUserId(UPDATED_APP_USER_ID);

        restFCMTokenMockMvc.perform(put("/api/fcm-tokens")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedFCMToken)))
            .andExpect(status().isOk());

        // Validate the FCMToken in the database
        List<FCMToken> fCMTokenList = fCMTokenRepository.findAll();
        assertThat(fCMTokenList).hasSize(databaseSizeBeforeUpdate);
        FCMToken testFCMToken = fCMTokenList.get(fCMTokenList.size() - 1);
        assertThat(testFCMToken.getToken()).isEqualTo(UPDATED_TOKEN);
        assertThat(testFCMToken.getAppUserId()).isEqualTo(UPDATED_APP_USER_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingFCMToken() throws Exception {
        int databaseSizeBeforeUpdate = fCMTokenRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFCMTokenMockMvc.perform(put("/api/fcm-tokens")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fCMToken)))
            .andExpect(status().isBadRequest());

        // Validate the FCMToken in the database
        List<FCMToken> fCMTokenList = fCMTokenRepository.findAll();
        assertThat(fCMTokenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFCMToken() throws Exception {
        // Initialize the database
        fCMTokenService.save(fCMToken);

        int databaseSizeBeforeDelete = fCMTokenRepository.findAll().size();

        // Delete the fCMToken
        restFCMTokenMockMvc.perform(delete("/api/fcm-tokens/{id}", fCMToken.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FCMToken> fCMTokenList = fCMTokenRepository.findAll();
        assertThat(fCMTokenList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
