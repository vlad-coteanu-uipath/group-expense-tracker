package org.fmi.unibuc.service.reporting.settlestrategy;

import org.fmi.unibuc.service.dto.SettleItemDTO;

import java.util.List;

public class NoneSettleStrategy implements SettleStrategy {
    @Override
    public List<SettleItemDTO> simplifySettleItems(List<SettleItemDTO> settleItemDTOList) {
        return settleItemDTOList;
    }
}
