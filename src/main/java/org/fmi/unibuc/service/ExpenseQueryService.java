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

import org.fmi.unibuc.domain.Expense;
import org.fmi.unibuc.domain.*; // for static metamodels
import org.fmi.unibuc.repository.ExpenseRepository;
import org.fmi.unibuc.service.dto.ExpenseCriteria;
import org.fmi.unibuc.service.dto.ExpenseDTO;
import org.fmi.unibuc.service.mapper.ExpenseMapper;

/**
 * Service for executing complex queries for {@link Expense} entities in the database.
 * The main input is a {@link ExpenseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ExpenseDTO} or a {@link Page} of {@link ExpenseDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExpenseQueryService extends QueryService<Expense> {

    private final Logger log = LoggerFactory.getLogger(ExpenseQueryService.class);

    private final ExpenseRepository expenseRepository;

    private final ExpenseMapper expenseMapper;

    public ExpenseQueryService(ExpenseRepository expenseRepository, ExpenseMapper expenseMapper) {
        this.expenseRepository = expenseRepository;
        this.expenseMapper = expenseMapper;
    }

    /**
     * Return a {@link List} of {@link ExpenseDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ExpenseDTO> findByCriteria(ExpenseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Expense> specification = createSpecification(criteria);
        return expenseMapper.toDto(expenseRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ExpenseDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExpenseDTO> findByCriteria(ExpenseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Expense> specification = createSpecification(criteria);
        return expenseRepository.findAll(specification, page)
            .map(expenseMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExpenseCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Expense> specification = createSpecification(criteria);
        return expenseRepository.count(specification);
    }

    /**
     * Function to convert {@link ExpenseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Expense> createSpecification(ExpenseCriteria criteria) {
        Specification<Expense> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Expense_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Expense_.description));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAmount(), Expense_.amount));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), Expense_.type));
            }
            if (criteria.getTripId() != null) {
                specification = specification.and(buildSpecification(criteria.getTripId(),
                    root -> root.join(Expense_.trip, JoinType.LEFT).get(Trip_.id)));
            }
            if (criteria.getCreatedById() != null) {
                specification = specification.and(buildSpecification(criteria.getCreatedById(),
                    root -> root.join(Expense_.createdBy, JoinType.LEFT).get(AppUser_.id)));
            }
            if (criteria.getParticipantsId() != null) {
                specification = specification.and(buildSpecification(criteria.getParticipantsId(),
                    root -> root.join(Expense_.participants, JoinType.LEFT).get(AppUser_.id)));
            }
        }
        return specification;
    }
}
