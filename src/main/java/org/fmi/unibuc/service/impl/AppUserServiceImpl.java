package org.fmi.unibuc.service.impl;

import org.fmi.unibuc.domain.Trip;
import org.fmi.unibuc.service.AppUserService;
import org.fmi.unibuc.domain.AppUser;
import org.fmi.unibuc.repository.AppUserRepository;
import org.fmi.unibuc.service.dto.AppUserDTO;
import org.fmi.unibuc.service.dto.ExtendedUserDTO;
import org.fmi.unibuc.service.dto.TripDTO;
import org.fmi.unibuc.service.mapper.AppUserMapper;
import org.fmi.unibuc.service.mapper.TripMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link AppUser}.
 */
@Service
@Transactional
public class AppUserServiceImpl implements AppUserService {

    private final Logger log = LoggerFactory.getLogger(AppUserServiceImpl.class);

    private final AppUserRepository appUserRepository;

    private final AppUserMapper appUserMapper;

    @Autowired
    private TripMapper tripMapper;

    public AppUserServiceImpl(AppUserRepository appUserRepository, AppUserMapper appUserMapper) {
        this.appUserRepository = appUserRepository;
        this.appUserMapper = appUserMapper;
    }

    @Override
    public AppUserDTO save(AppUserDTO appUserDTO) {
        log.debug("Request to save AppUser : {}", appUserDTO);
        AppUser appUser = appUserMapper.toEntity(appUserDTO);
        appUser = appUserRepository.save(appUser);
        return appUserMapper.toDto(appUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppUserDTO> findAll() {
        log.debug("Request to get all AppUsers");
        return appUserRepository.findAllWithEagerRelationships().stream()
            .map(appUserMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    public Page<AppUserDTO> findAllWithEagerRelationships(Pageable pageable) {
        return appUserRepository.findAllWithEagerRelationships(pageable).map(appUserMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AppUserDTO> findOne(Long id) {
        log.debug("Request to get AppUser : {}", id);
        return appUserRepository.findOneWithEagerRelationships(id)
            .map(appUserMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AppUser : {}", id);
        appUserRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AppUserDTO> findOneByUser(Long id) {
        log.debug("Request to get AppUser by user Id: {}", id);
        return appUserRepository.findOneByUserId(id)
            .map(appUserMapper::toDto);
    }

    @Override
    @Transactional
    public List<ExtendedUserDTO> getAllCandidates(Long appUserId)
    {
        // Step 1 - get this appUserId trip Ids
        Optional<AppUser> appUser = appUserRepository.findOneWithEagerRelationships(appUserId);
        if(!appUser.isPresent()) {
            return new ArrayList<>();
        }
        Set<Long> tripIds = appUser.get().getTrips().stream().map(t -> t.getId()).collect(Collectors.toSet());

        // Step 2 - get all appUsers with relationships
        List<AppUser> candidates = appUserRepository.findAllWithEagerRelationships();

        // Step 3 - compute priority
        Map<Long, Integer> appUserToPriorityMap = new HashMap<>();
        for(AppUser candidate : candidates) {
            int priority = 0;
            Set<Long> currentCandidateTripIds = candidate.getTrips().stream().map(t -> t.getId()).collect(Collectors.toSet());
            currentCandidateTripIds.retainAll(tripIds);
            priority = currentCandidateTripIds.size();
            appUserToPriorityMap.put(candidate.getId(), priority);
        }

        // Step 4 - order
        candidates.sort(new Comparator<AppUser>() {
            @Override
            public int compare(AppUser o1, AppUser o2) {
                return appUserToPriorityMap.get(o2.getId()) - appUserToPriorityMap.get(o1.getId());
            }
        });

        return candidates.stream().map(candidate ->
            new ExtendedUserDTO (
                candidate.getUser().getId(),
                candidate.getUser().getLogin(),
                candidate.getUser().getFirstName(),
                candidate.getUser().getLastName(),
                candidate.getId())
        ).collect(Collectors.toList());
    }

    public List<TripDTO> findTripsForUser(Long appUserId) {
        Optional<AppUser> appUser = appUserRepository.findById(appUserId);
        return appUser.map(user -> user.getTrips().stream().sorted(new Comparator<Trip>() {
            @Override
            public int compare(Trip o1, Trip o2) {
                return o2.getCreatedDate().compareTo(o1.getCreatedDate());
            }
        }).map(tripMapper::toDto).collect(Collectors.toList())).orElseGet(ArrayList::new);

    }
}
