package org.fmi.unibuc.repository;

import org.fmi.unibuc.domain.FCMToken;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the FCMToken entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FCMTokenRepository extends JpaRepository<FCMToken, Long> {

    Optional<FCMToken> findOneByAppUserId(Long appUserId);

}
