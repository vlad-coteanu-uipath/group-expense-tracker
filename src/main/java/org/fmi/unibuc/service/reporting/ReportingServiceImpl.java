package org.fmi.unibuc.service.reporting;

import org.fmi.unibuc.domain.AppUser;
import org.fmi.unibuc.domain.Expense;
import org.fmi.unibuc.repository.ExpenseRepository;
import org.fmi.unibuc.service.dto.SettleItemDTO;
import org.fmi.unibuc.service.reporting.settlestrategy.SettleStrategy;
import org.fmi.unibuc.service.reporting.settlestrategy.SettleStrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class ReportingServiceImpl implements ReportingService {

    @Autowired
    private ExpenseRepository expenseRepository;

    public List<SettleItemDTO> getSettleItems(SettleLevel settleLevel) {

        // Get all the settle items
        // For each expense like A to group {B, C} of amount D
        // there will be added the following settleItemDTOS
        // B to A of amount D/2
        // C to A of amount D/2
        List<SettleItemDTO> settleItemDTOList = getAllSettleItems();

        // Use factory to get a strategy
        SettleStrategy settleStrategy = SettleStrategyFactory.INSTANCE.getSettleStrategy(settleLevel);

        // Call the algorithm based on the strategy
        return SettleHelper.INSTANCE.setSettleStrategy(settleStrategy).settle(settleItemDTOList);
    }

    private List<SettleItemDTO> getAllSettleItems() {
        List<Expense> expenses = expenseRepository.findAll();
        List<SettleItemDTO> settleItemDTOList = new ArrayList<>();
        for (Expense expense : expenses) {
            settleItemDTOList.addAll(fromExpense(expense));
        }
        return settleItemDTOList;
    }

    private List<SettleItemDTO> fromExpense(Expense expense) {
        List<SettleItemDTO> settleItemDTOList = new ArrayList<SettleItemDTO>();
        AppUser createdBy = expense.getCreatedBy();
        Set<AppUser> participants = expense.getParticipants();
        if(participants.size() == 0) {
            return settleItemDTOList;
        }

        BigDecimal dividedAmount = expense.getAmount().divide(BigDecimal.valueOf(participants.size()), 2, BigDecimal.ROUND_FLOOR);
        for (AppUser appUser : participants) {
            SettleItemDTO settleItemDTO = new SettleItemDTO(
                appUser.getId(),
                appUser.getUser().getFirstName(),
                appUser.getUser().getLastName(),
                createdBy.getId(),
                createdBy.getUser().getFirstName(),
                createdBy.getUser().getLastName(),
                dividedAmount
            );
            settleItemDTOList.add(settleItemDTO);
        }
        return settleItemDTOList;
    }

}
