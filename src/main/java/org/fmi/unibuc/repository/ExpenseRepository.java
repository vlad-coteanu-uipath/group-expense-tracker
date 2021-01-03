package org.fmi.unibuc.repository;

import org.fmi.unibuc.domain.Expense;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Expense entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long>, JpaSpecificationExecutor<Expense> {
}
