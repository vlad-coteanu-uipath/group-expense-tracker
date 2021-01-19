package org.fmi.unibuc.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import org.fmi.unibuc.service.AppUserService;
import org.fmi.unibuc.service.TripService;
import org.fmi.unibuc.service.dto.*;
import org.fmi.unibuc.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class GETCustomResource {

    private final Logger log = LoggerFactory.getLogger(GETCustomResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private TripService tripService;

    /**
     * {@code GET  /custom/candidates/{}:appUserId} : get all the users to a trip in descending order of the
     * "priority". A user is more important if he has more common trips to the user that access this endpoint
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dtos in body.
     */
    @GetMapping("/custom/candidates/{appUserId}")
    public ResponseEntity<List<ExtendedUserDTO>> getAllCandidates(@PathVariable Long appUserId) {
        log.debug("REST request to get candidates for appUserId: {}", appUserId);
        List<ExtendedUserDTO> extendedUserDTOList = appUserService.getAllCandidates(appUserId);
        return ResponseEntity.ok().body(extendedUserDTOList);
    }

    /**
     * {@code POST  /custom/create-trip : creates a trip based on createTripDTO
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the id of the newly created trip
     */
    @PostMapping("/custom/create-trip")
    public ResponseEntity<Long> createTrip(@RequestBody CreateTripDTO createTripDTO) {
        log.debug("REST request to save Trip : {}", createTripDTO);
        Long resultId = tripService.createTrip(createTripDTO);
        return ResponseEntity.ok().body(resultId);
    }

    /**
     * {@code GET  /custom/trips/participant/{:appUserId} gets all the trips where appUserId is participant
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dtos in body.
     */
    @GetMapping("/custom/trips/participant/{appUserId}")
    public ResponseEntity<List<TripDTO>> getAllTripsForAppUserId(@PathVariable Long appUserId) {
        log.debug("REST request to get candidates for appUserId: {}", appUserId);
        List<TripDTO> tripList = appUserService.findTripsForUser(appUserId);
        return ResponseEntity.ok().body(tripList);
    }

}