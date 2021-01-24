package org.fmi.unibuc.web.rest;

import org.fmi.unibuc.domain.FCMToken;
import org.fmi.unibuc.service.FCMTokenService;
import org.fmi.unibuc.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link org.fmi.unibuc.domain.FCMToken}.
 */
@RestController
@RequestMapping("/api")
public class FCMTokenResource {

    private final Logger log = LoggerFactory.getLogger(FCMTokenResource.class);

    private static final String ENTITY_NAME = "fCMToken";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FCMTokenService fCMTokenService;

    public FCMTokenResource(FCMTokenService fCMTokenService) {
        this.fCMTokenService = fCMTokenService;
    }

    /**
     * {@code POST  /fcm-tokens} : Create a new fCMToken.
     *
     * @param fCMToken the fCMToken to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fCMToken, or with status {@code 400 (Bad Request)} if the fCMToken has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fcm-tokens")
    public ResponseEntity<FCMToken> createFCMToken(@RequestBody FCMToken fCMToken) throws URISyntaxException {
        log.debug("REST request to save FCMToken : {}", fCMToken);
        if (fCMToken.getId() != null) {
            throw new BadRequestAlertException("A new fCMToken cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FCMToken result = fCMTokenService.save(fCMToken);
        return ResponseEntity.created(new URI("/api/fcm-tokens/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fcm-tokens} : Updates an existing fCMToken.
     *
     * @param fCMToken the fCMToken to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fCMToken,
     * or with status {@code 400 (Bad Request)} if the fCMToken is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fCMToken couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fcm-tokens")
    public ResponseEntity<FCMToken> updateFCMToken(@RequestBody FCMToken fCMToken) throws URISyntaxException {
        log.debug("REST request to update FCMToken : {}", fCMToken);
        if (fCMToken.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FCMToken result = fCMTokenService.save(fCMToken);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, fCMToken.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /fcm-tokens} : get all the fCMTokens.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fCMTokens in body.
     */
    @GetMapping("/fcm-tokens")
    public List<FCMToken> getAllFCMTokens() {
        log.debug("REST request to get all FCMTokens");
        return fCMTokenService.findAll();
    }

    /**
     * {@code GET  /fcm-tokens/:id} : get the "id" fCMToken.
     *
     * @param id the id of the fCMToken to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fCMToken, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fcm-tokens/{id}")
    public ResponseEntity<FCMToken> getFCMToken(@PathVariable Long id) {
        log.debug("REST request to get FCMToken : {}", id);
        Optional<FCMToken> fCMToken = fCMTokenService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fCMToken);
    }

    /**
     * {@code DELETE  /fcm-tokens/:id} : delete the "id" fCMToken.
     *
     * @param id the id of the fCMToken to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fcm-tokens/{id}")
    public ResponseEntity<Void> deleteFCMToken(@PathVariable Long id) {
        log.debug("REST request to delete FCMToken : {}", id);
        fCMTokenService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
