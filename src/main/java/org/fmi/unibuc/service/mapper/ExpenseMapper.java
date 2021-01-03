package org.fmi.unibuc.service.mapper;


import org.fmi.unibuc.domain.*;
import org.fmi.unibuc.service.dto.ExpenseDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Expense} and its DTO {@link ExpenseDTO}.
 */
@Mapper(componentModel = "spring", uses = {TripMapper.class, AppUserMapper.class})
public interface ExpenseMapper extends EntityMapper<ExpenseDTO, Expense> {

    @Mapping(source = "trip.id", target = "tripId")
    @Mapping(source = "createdBy.id", target = "createdById")
    ExpenseDTO toDto(Expense expense);

    @Mapping(source = "tripId", target = "trip")
    @Mapping(source = "createdById", target = "createdBy")
    @Mapping(target = "participants", ignore = true)
    @Mapping(target = "removeParticipants", ignore = true)
    Expense toEntity(ExpenseDTO expenseDTO);

    default Expense fromId(Long id) {
        if (id == null) {
            return null;
        }
        Expense expense = new Expense();
        expense.setId(id);
        return expense;
    }
}
