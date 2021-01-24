package org.fmi.unibuc.service;

import org.fmi.unibuc.domain.FCMToken;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link FCMToken}.
 */
public interface FCMTokenService {

    /**
     * Save a fCMToken.
     *
     * @param fCMToken the entity to save.
     * @return the persisted entity.
     */
    FCMToken save(FCMToken fCMToken);

    /**
     * Get all the fCMTokens.
     *
     * @return the list of entities.
     */
    List<FCMToken> findAll();


    /**
     * Get the "id" fCMToken.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FCMToken> findOne(Long id);

    /**
     * Delete the "id" fCMToken.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Optional<FCMToken> findOneByAppUserId(Long appUserId);
}
