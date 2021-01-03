package org.fmi.unibuc.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import org.fmi.unibuc.domain.Trip;
import org.fmi.unibuc.domain.*; // for static metamodels
import org.fmi.unibuc.repository.TripRepository;
import org.fmi.unibuc.service.dto.TripCriteria;
import org.fmi.unibuc.service.dto.TripDTO;
import org.fmi.unibuc.service.mapper.TripMapper;

/**
 * Service for executing complex queries for {@link Trip} entities in the database.
 * The main input is a {@link TripCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TripDTO} or a {@link Page} of {@link TripDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TripQueryService extends QueryService<Trip> {

    private final Logger log = LoggerFactory.getLogger(TripQueryService.class);

    private final TripRepository tripRepository;

    private final TripMapper tripMapper;

    public TripQueryService(TripRepository tripRepository, TripMapper tripMapper) {
        this.tripRepository = tripRepository;
        this.tripMapper = tripMapper;
    }

    /**
     * Return a {@link List} of {@link TripDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TripDTO> findByCriteria(TripCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Trip> specification = createSpecification(criteria);
        return tripMapper.toDto(tripRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TripDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TripDTO> findByCriteria(TripCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Trip> specification = createSpecification(criteria);
        return tripRepository.findAll(specification, page)
            .map(tripMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TripCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Trip> specification = createSpecification(criteria);
        return tripRepository.count(specification);
    }

    /**
     * Function to convert {@link TripCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Trip> createSpecification(TripCriteria criteria) {
        Specification<Trip> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Trip_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Trip_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Trip_.description));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Trip_.createdDate));
            }
            if (criteria.getExpensesId() != null) {
                specification = specification.and(buildSpecification(criteria.getExpensesId(),
                    root -> root.join(Trip_.expenses, JoinType.LEFT).get(Expense_.id)));
            }
            if (criteria.getCreatedById() != null) {
                specification = specification.and(buildSpecification(criteria.getCreatedById(),
                    root -> root.join(Trip_.createdBy, JoinType.LEFT).get(AppUser_.id)));
            }
            if (criteria.getParticipantsId() != null) {
                specification = specification.and(buildSpecification(criteria.getParticipantsId(),
                    root -> root.join(Trip_.participants, JoinType.LEFT).get(AppUser_.id)));
            }
        }
        return specification;
    }
}
