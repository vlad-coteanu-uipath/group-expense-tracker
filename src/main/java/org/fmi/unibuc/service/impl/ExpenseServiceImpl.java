package org.fmi.unibuc.service.impl;

import org.fmi.unibuc.service.ExpenseService;
import org.fmi.unibuc.domain.Expense;
import org.fmi.unibuc.repository.ExpenseRepository;
import org.fmi.unibuc.service.dto.ExpenseDTO;
import org.fmi.unibuc.service.mapper.ExpenseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Expense}.
 */
@Service
@Transactional
public class ExpenseServiceImpl implements ExpenseService {

    private final Logger log = LoggerFactory.getLogger(ExpenseServiceImpl.class);

    private final ExpenseRepository expenseRepository;

    private final ExpenseMapper expenseMapper;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository, ExpenseMapper expenseMapper) {
        this.expenseRepository = expenseRepository;
        this.expenseMapper = expenseMapper;
    }

    @Override
    public ExpenseDTO save(ExpenseDTO expenseDTO) {
        log.debug("Request to save Expense : {}", expenseDTO);
        Expense expense = expenseMapper.toEntity(expenseDTO);
        expense = expenseRepository.save(expense);
        return expenseMapper.toDto(expense);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExpenseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Expenses");
        return expenseRepository.findAll(pageable)
            .map(expenseMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<ExpenseDTO> findOne(Long id) {
        log.debug("Request to get Expense : {}", id);
        return expenseRepository.findById(id)
            .map(expenseMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Expense : {}", id);
        expenseRepository.deleteById(id);
    }
}
