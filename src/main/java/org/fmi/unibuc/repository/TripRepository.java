package org.fmi.unibuc.repository;

import org.fmi.unibuc.domain.Trip;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Trip entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TripRepository extends JpaRepository<Trip, Long>, JpaSpecificationExecutor<Trip> {
}
