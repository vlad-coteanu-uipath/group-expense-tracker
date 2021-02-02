package org.fmi.unibuc.service;

import org.fmi.unibuc.service.dto.CreateExpenseDTO;
import org.fmi.unibuc.service.dto.ExpenseDTO;

import org.fmi.unibuc.service.dto.ExtendedExpenseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link org.fmi.unibuc.domain.Expense}.
 */
public interface ExpenseService {

    /**
     * Save a expense.
     *
     * @param expenseDTO the entity to save.
     * @return the persisted entity.
     */
    ExpenseDTO save(ExpenseDTO expenseDTO);

    /**
     * Get all the expenses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ExpenseDTO> findAll(Pageable pageable);


    /**
     * Get the "id" expense.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExpenseDTO> findOne(Long id);

    /**
     * Delete the "id" expense.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    ExtendedExpenseDTO getExpenseWithCompleteDetails(Long expenseId);

    boolean updateExtendedExpense(CreateExpenseDTO createExpenseDTO);

    Long createExpense(CreateExpenseDTO createExpenseDTO);
}
