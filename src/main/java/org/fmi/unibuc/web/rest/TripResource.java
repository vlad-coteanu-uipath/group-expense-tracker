package org.fmi.unibuc.web.rest;

import org.fmi.unibuc.service.TripService;
import org.fmi.unibuc.web.rest.errors.BadRequestAlertException;
import org.fmi.unibuc.service.dto.TripDTO;
import org.fmi.unibuc.service.dto.TripCriteria;
import org.fmi.unibuc.service.TripQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link org.fmi.unibuc.domain.Trip}.
 */
@RestController
@RequestMapping("/api")
public class TripResource {

    private final Logger log = LoggerFactory.getLogger(TripResource.class);

    private static final String ENTITY_NAME = "trip";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TripService tripService;

    private final TripQueryService tripQueryService;

    public TripResource(TripService tripService, TripQueryService tripQueryService) {
        this.tripService = tripService;
        this.tripQueryService = tripQueryService;
    }

    /**
     * {@code POST  /trips} : Create a new trip.
     *
     * @param tripDTO the tripDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tripDTO, or with status {@code 400 (Bad Request)} if the trip has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/trips")
    public ResponseEntity<TripDTO> createTrip(@RequestBody TripDTO tripDTO) throws URISyntaxException {
        log.debug("REST request to save Trip : {}", tripDTO);
        if (tripDTO.getId() != null) {
            throw new BadRequestAlertException("A new trip cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TripDTO result = tripService.save(tripDTO);
        return ResponseEntity.created(new URI("/api/trips/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /trips} : Updates an existing trip.
     *
     * @param tripDTO the tripDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tripDTO,
     * or with status {@code 400 (Bad Request)} if the tripDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tripDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/trips")
    public ResponseEntity<TripDTO> updateTrip(@RequestBody TripDTO tripDTO) throws URISyntaxException {
        log.debug("REST request to update Trip : {}", tripDTO);
        if (tripDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TripDTO result = tripService.save(tripDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tripDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /trips} : get all the trips.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trips in body.
     */
    @GetMapping("/trips")
    public ResponseEntity<List<TripDTO>> getAllTrips(TripCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Trips by criteria: {}", criteria);
        Page<TripDTO> page = tripQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /trips/count} : count all the trips.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/trips/count")
    public ResponseEntity<Long> countTrips(TripCriteria criteria) {
        log.debug("REST request to count Trips by criteria: {}", criteria);
        return ResponseEntity.ok().body(tripQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /trips/:id} : get the "id" trip.
     *
     * @param id the id of the tripDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tripDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/trips/{id}")
    public ResponseEntity<TripDTO> getTrip(@PathVariable Long id) {
        log.debug("REST request to get Trip : {}", id);
        Optional<TripDTO> tripDTO = tripService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tripDTO);
    }

    /**
     * {@code DELETE  /trips/:id} : delete the "id" trip.
     *
     * @param id the id of the tripDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/trips/{id}")
    public ResponseEntity<Void> deleteTrip(@PathVariable Long id) {
        log.debug("REST request to delete Trip : {}", id);
        tripService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
