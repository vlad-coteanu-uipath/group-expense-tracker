package org.fmi.unibuc.web.rest;

import org.fmi.unibuc.service.AppUserService;
import org.fmi.unibuc.service.TripService;
import org.fmi.unibuc.service.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
     * @return
     */
    @PostMapping("/custom/create-trip")
    public ResponseEntity<Object> createTrip(@RequestBody CreateTripDTO createTripDTO) {
        log.debug("REST request to save Trip : {}", createTripDTO);
        Long resultId = tripService.createTrip(createTripDTO);
        return ResponseEntity.ok().body(JSONObject.wrap(resultId));
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

    /**
     * {@code GET  /custom/trips/trip-details/{:tripId} gets trip extended details for trip ID
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dtos in body.
     */
    @GetMapping("/custom/trips/trip-details/{tripId}")
    public ResponseEntity<ExtendedTripDTO> getTripDetailsForTripId(@PathVariable Long tripId) {
        log.debug("REST request to get trip extended details for tripId: {}", tripId);
        ExtendedTripDTO extendedTripDTO = tripService.getTripWithCompleteDetails(tripId);
        return ResponseEntity.ok().body(extendedTripDTO);
    }

    /**
     * {@code PUT  /custom/update-trip-participants/{tripId} : updates trip participants
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)}
     * @return
     */
    @PutMapping("/custom/update-trip-participants/{tripId}")
    public ResponseEntity<List<Boolean>> updateTripParticipants(@RequestBody long[] newTripParticipantsId, @PathVariable Long tripId) {
        log.debug("REST request to update trip members : {}", tripId);
        tripService.updateTripParticipants(tripId, newTripParticipantsId);
        List<Boolean> respList = new ArrayList<>();
        respList.add(true);
        return ResponseEntity.ok().body(respList);
    }

    /**
     * {@code POST  /custom/create-expense : creates an expense based on createExpenseDTO
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the id of the newly created trip
     * @return
     */
    @PostMapping("/custom/create-expense")
    public ResponseEntity<Object> createExpense(@RequestBody CreateExpenseDTO createExpenseDTO) {
        log.debug("REST request to save Expense : {}", createExpenseDTO);
        Long resultId = tripService.createExpense(createExpenseDTO);
        return ResponseEntity.ok().body(JSONObject.wrap(resultId));
    }

}
