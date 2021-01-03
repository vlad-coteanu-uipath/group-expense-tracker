package org.fmi.unibuc.service.impl;

import org.fmi.unibuc.service.AppUserService;
import org.fmi.unibuc.domain.AppUser;
import org.fmi.unibuc.repository.AppUserRepository;
import org.fmi.unibuc.service.dto.AppUserDTO;
import org.fmi.unibuc.service.mapper.AppUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
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
}
