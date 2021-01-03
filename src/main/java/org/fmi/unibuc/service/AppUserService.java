package org.fmi.unibuc.service;

import org.fmi.unibuc.service.dto.AppUserDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link org.fmi.unibuc.domain.AppUser}.
 */
public interface AppUserService {

    /**
     * Save a appUser.
     *
     * @param appUserDTO the entity to save.
     * @return the persisted entity.
     */
    AppUserDTO save(AppUserDTO appUserDTO);

    /**
     * Get all the appUsers.
     *
     * @return the list of entities.
     */
    List<AppUserDTO> findAll();

    /**
     * Get all the appUsers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<AppUserDTO> findAllWithEagerRelationships(Pageable pageable);


    /**
     * Get the "id" appUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AppUserDTO> findOne(Long id);

    /**
     * Delete the "id" appUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
