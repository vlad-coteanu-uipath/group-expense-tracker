package org.fmi.unibuc.service.impl;

import org.fmi.unibuc.domain.AppUser;
import org.fmi.unibuc.domain.Trip;
import org.fmi.unibuc.domain.User;
import org.fmi.unibuc.domain.enumeration.ExpenseType;
import org.fmi.unibuc.repository.AppUserRepository;
import org.fmi.unibuc.repository.TripRepository;
import org.fmi.unibuc.service.AppUserService;
import org.fmi.unibuc.service.ExpenseService;
import org.fmi.unibuc.domain.Expense;
import org.fmi.unibuc.repository.ExpenseRepository;
import org.fmi.unibuc.service.dto.CreateExpenseDTO;
import org.fmi.unibuc.service.dto.ExpenseDTO;
import org.fmi.unibuc.service.dto.ExtendedExpenseDTO;
import org.fmi.unibuc.service.dto.ExtendedUserDTO;
import org.fmi.unibuc.service.mapper.ExpenseMapper;
import org.fmi.unibuc.service.notifications.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Service Implementation for managing {@link Expense}.
 */
@Service
@Transactional
public class ExpenseServiceImpl implements ExpenseService {

    private final Logger log = LoggerFactory.getLogger(ExpenseServiceImpl.class);

    private final ExpenseRepository expenseRepository;

    private final ExpenseMapper expenseMapper;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private  NotificationService notificationService;

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

    @Override
    public ExtendedExpenseDTO getExpenseWithCompleteDetails(Long expenseId) {

        Optional<Expense> expenseOptional = expenseRepository.findById(expenseId);
        if(!expenseOptional.isPresent()) {
            return null;
        }
        Expense expense = expenseOptional.get();

        ExtendedExpenseDTO extendedExpenseDTO = new ExtendedExpenseDTO();
        extendedExpenseDTO.setExpenseId(expense.getId());
        extendedExpenseDTO.setDescription(expense.getDescription());
        extendedExpenseDTO.setExpenseType(expense.getType());
        extendedExpenseDTO.setAmount(expense.getAmount().doubleValue());

        Set<ExtendedUserDTO> expenseParticipants = new HashSet<>();
        for (AppUser participant : expense.getParticipants()) {
            ExtendedUserDTO extendedUserDTO = new ExtendedUserDTO(
                participant.getUser().getId(),
                participant.getUser().getLogin(),
                participant.getUser().getFirstName(),
                participant.getUser().getLastName(),
                participant.getId());
            expenseParticipants.add(extendedUserDTO);
        }
        extendedExpenseDTO.setExpenseParticipants(expenseParticipants);

        return extendedExpenseDTO;
    }

    @Override
    public boolean updateExtendedExpense(CreateExpenseDTO createExpenseDTO) {

        Optional<Expense> expenseOptional = expenseRepository.findById(createExpenseDTO.getId());
        if(!expenseOptional.isPresent()) {
            return false;
        }

        Expense expense = expenseOptional.get();
        if(createExpenseDTO.getDescription() != null && createExpenseDTO.getDescription() != "") {
            expense.setDescription(createExpenseDTO.getDescription());
        }

        if(createExpenseDTO.getAmount() != 0) {
            expense.setAmount(new BigDecimal(createExpenseDTO.getAmount()));
        }

        Set<Long> newParticipantsIds = new HashSet<Long>();
        if(createExpenseDTO.getParticipantsAppUserId() != null && createExpenseDTO.getParticipantsAppUserId().length != 0) {


            for(int i = 0; i < createExpenseDTO.getParticipantsAppUserId().length; i++) {
                newParticipantsIds.add(createExpenseDTO.getParticipantsAppUserId()[i]);
            }
            Set<AppUser> currentParticipants = expense.getParticipants();

            // Handle participants removed from expense
            for(AppUser appUser : currentParticipants) {
                if(!newParticipantsIds.contains(appUser.getId())) {
                    appUser.getExpenses().remove(expense);
                }
            }

            for(Long appUserId : newParticipantsIds) {
                if(!newParticipantsIds.contains(appUserId)) {
                    Optional<AppUser> candidateOpt = appUserRepository.findById(appUserId);
                    if (candidateOpt.isPresent()) {
                        AppUser candidate = candidateOpt.get();
                        candidate.getExpenses().add(expense);
                        appUserRepository.save(candidate);
                    }
                }
            }

        }

        expenseRepository.save(expense);

        String tripName = expense.getTrip().getName();
        String expenseName = expense.getDescription();
        String expenseValuePerUser = String.valueOf(createExpenseDTO.getAmount() / createExpenseDTO.getParticipantsAppUserId().length);
        User createdByUser = expense.getCreatedBy().getUser();
        String createdByDetails = createdByUser.getFirstName() + " " + createdByUser.getLastName() + " ( " + createdByUser.getLogin() + ")";

        if(expense.getType() == ExpenseType.GROUP) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    notificationService.sendUpdateExpenseNotification(
                        createdByDetails,
                        newParticipantsIds,
                        tripName,
                        expenseName,
                        expenseValuePerUser);
                }
            }).start();
        }

        return true;
    }

    @Override
    public Long createExpense(CreateExpenseDTO createExpenseDTO) {
        Expense expense = new Expense();

        Optional<AppUser> appUserOpt = appUserRepository.findById(createExpenseDTO.getCreatedBy());
        if(!appUserOpt.isPresent()) {
            return null;
        }

        Optional<Trip> tripOptional = tripRepository.findById(createExpenseDTO.getTripId());
        if(!tripOptional.isPresent()) {
            return null;
        }

        expense.setCreatedBy(appUserOpt.get());
        expense.setDescription(createExpenseDTO.getDescription());
        expense.setAmount(new BigDecimal(createExpenseDTO.getAmount()));
        expense.setTrip(tripOptional.get());
        expense.setType(createExpenseDTO.getExpenseType());
        expense = expenseRepository.save(expense);

        Set<Long> candidatesIds = new HashSet<>();
        for(int i = 0; i < createExpenseDTO.getParticipantsAppUserId().length; i++) {
            Optional<AppUser> candidateOpt = appUserRepository.findById(createExpenseDTO.getParticipantsAppUserId()[i]);
            if(candidateOpt.isPresent()) {
                AppUser candidate = candidateOpt.get();
                candidate.getExpenses().add(expense);
                appUserRepository.save(candidate);
                candidatesIds.add(candidate.getId());
            }
        }

        String tripName = tripOptional.get().getName();
        String expenseName = expense.getDescription();
        String expenseValuePerUser = String.valueOf(createExpenseDTO.getAmount() / createExpenseDTO.getParticipantsAppUserId().length);
        User createdByUser = appUserOpt.get().getUser();
        String createdByDetails = createdByUser.getFirstName() + " " + createdByUser.getLastName() + " ( " + createdByUser.getLogin() + ")";

        if(expense.getType() == ExpenseType.GROUP) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    notificationService.sendCreateExpenseNotification(
                        appUserOpt.get().getId(),
                        createdByDetails,
                        candidatesIds,
                        tripName,
                        expenseName,
                        expenseValuePerUser);
                }
            }).start();
        }

        return expense.getId();
    }


}
