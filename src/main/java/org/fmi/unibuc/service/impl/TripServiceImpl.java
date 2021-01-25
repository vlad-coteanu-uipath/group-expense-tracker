package org.fmi.unibuc.service.impl;

import org.fmi.unibuc.domain.AppUser;
import org.fmi.unibuc.domain.Expense;
import org.fmi.unibuc.domain.User;
import org.fmi.unibuc.repository.AppUserRepository;
import org.fmi.unibuc.service.AppUserService;
import org.fmi.unibuc.service.TripService;
import org.fmi.unibuc.domain.Trip;
import org.fmi.unibuc.repository.TripRepository;
import org.fmi.unibuc.service.dto.CreateTripDTO;
import org.fmi.unibuc.service.dto.ExtendedTripDTO;
import org.fmi.unibuc.service.dto.ExtendedUserDTO;
import org.fmi.unibuc.service.dto.TripDTO;
import org.fmi.unibuc.service.mapper.ExpenseMapper;
import org.fmi.unibuc.service.mapper.TripMapper;
import org.fmi.unibuc.service.notifications.NotificationService;
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
import java.util.stream.Collectors;

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

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ExpenseMapper expenseMapper;

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
        trip = tripRepository.save(trip);

        Set<Long> candidatesIds = new HashSet<>();
        for(int i = 0; i < createTripDTO.getParticipantsAppUserId().length; i++) {
            Optional<AppUser> candidateOpt = appUserRepository.findById(createTripDTO.getParticipantsAppUserId()[i]);
            if(candidateOpt.isPresent()) {
                AppUser candidate = candidateOpt.get();
                candidate.getTrips().add(trip);
                appUserRepository.save(candidate);
                candidatesIds.add(candidate.getId());
            }
        }

        String tripName = trip.getName();
        User createdByUser = appUserOpt.get().getUser();
        String createdByDetails = createdByUser.getFirstName() + " " + createdByUser.getLastName() + " ( " + createdByUser.getLogin() + ")";

        new Thread(new Runnable() {
            @Override
            public void run() {
                notificationService.sendCreateTripNotification(
                    appUserOpt.get().getId(),
                    createdByDetails,
                    candidatesIds,
                    tripName);
            }
        }).start();

        return trip.getId();
    }

    public ExtendedTripDTO getTripWithCompleteDetails(long tripId) {

        Optional<TripDTO> tripDTOOptional = this.findOne(tripId);
        if(!tripDTOOptional.isPresent()) {
            return null;
        }

        ExtendedTripDTO extendedTripDTO = ExtendedTripDTO.fromTripDTO(tripDTOOptional.get());

        // 1. Add created by as extended app user
        Optional<AppUser> appUserOptional = appUserRepository.findById(extendedTripDTO.getCreatedById());
        if(!appUserOptional.isPresent()) {
            return null;
        }
        AppUser createdByAppUser = appUserOptional.get();

        extendedTripDTO.setExtendedCreatedBy(new ExtendedUserDTO(
            createdByAppUser.getUser().getId(),
            createdByAppUser.getUser().getLogin(),
            createdByAppUser.getUser().getFirstName(),
            createdByAppUser.getUser().getLastName(),
            createdByAppUser.getId()));

        // 2. Add list of participants
        Trip trip = tripRepository.getOne(tripId);
        Set<ExtendedUserDTO> tripParticipants = new HashSet<>();
        for (AppUser participant : trip.getParticipants()) {
            ExtendedUserDTO extendedUserDTO = new ExtendedUserDTO(
                participant.getUser().getId(),
                participant.getUser().getLogin(),
                participant.getUser().getFirstName(),
                participant.getUser().getLastName(),
                participant.getId());
            tripParticipants.add(extendedUserDTO);
        }
        extendedTripDTO.setTripParticipants(tripParticipants);

        // 3. Add list of expenses
        Set<Expense> expenses = trip.getExpenses();
        extendedTripDTO.setTripExpenses(expenses.stream().map(expenseMapper::toDto).collect(Collectors.toSet()));

        return extendedTripDTO;
    }
}
