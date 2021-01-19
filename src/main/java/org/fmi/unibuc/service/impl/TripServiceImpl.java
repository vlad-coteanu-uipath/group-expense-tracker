package org.fmi.unibuc.service.impl;

import org.fmi.unibuc.domain.AppUser;
import org.fmi.unibuc.repository.AppUserRepository;
import org.fmi.unibuc.service.TripService;
import org.fmi.unibuc.domain.Trip;
import org.fmi.unibuc.repository.TripRepository;
import org.fmi.unibuc.service.dto.CreateTripDTO;
import org.fmi.unibuc.service.dto.TripDTO;
import org.fmi.unibuc.service.mapper.TripMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service Implementation for managing {@link Trip}.
 */
@Service
@Transactional
public class TripServiceImpl implements TripService {

    private final Logger log = LoggerFactory.getLogger(TripServiceImpl.class);

    private final TripRepository tripRepository;

    private final TripMapper tripMapper;

    @Autowired
    private AppUserRepository appUserRepository;

    public TripServiceImpl(TripRepository tripRepository, TripMapper tripMapper) {
        this.tripRepository = tripRepository;
        this.tripMapper = tripMapper;
    }

    @Override
    public TripDTO save(TripDTO tripDTO) {
        log.debug("Request to save Trip : {}", tripDTO);
        Trip trip = tripMapper.toEntity(tripDTO);
        trip = tripRepository.save(trip);
        return tripMapper.toDto(trip);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TripDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Trips");
        return tripRepository.findAll(pageable)
            .map(tripMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<TripDTO> findOne(Long id) {
        log.debug("Request to get Trip : {}", id);
        return tripRepository.findById(id)
            .map(tripMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Trip : {}", id);
        tripRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Long createTrip(CreateTripDTO createTripDTO) {

        Trip trip = new Trip();

        Optional<AppUser> appUserOpt = appUserRepository.findById(createTripDTO.getCreatedBy());
        if(!appUserOpt.isPresent()) {
            return null;
        }
        trip.setCreatedBy(appUserOpt.get());
        trip.setCreatedDate(LocalDate.now());
        trip.setDescription(createTripDTO.getDescription());
        trip.setName(createTripDTO.getTitle());

        Set<AppUser> candidates = new HashSet<>();
        for(int i = 0; i < createTripDTO.getParticipantsAppUserId().length; i++) {
            Optional<AppUser> candidateOpt = appUserRepository.findById(createTripDTO.getParticipantsAppUserId()[i]);
            if(candidateOpt.isPresent()) {
                candidates.add(appUserOpt.get());
            }
        }
        trip.setParticipants(candidates);
        trip = tripRepository.save(trip);
        return trip.getId();
    }
}
