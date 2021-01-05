package org.fmi.unibuc.service.reporting;

import org.fmi.unibuc.service.dto.SettleItemDTO;
import org.fmi.unibuc.service.reporting.settlestrategy.NoneSettleStrategy;
import org.fmi.unibuc.service.reporting.settlestrategy.SettleStrategy;

import java.util.List;

public class SettleHelper {

    public static final SettleHelper INSTANCE = new SettleHelper();

    private SettleStrategy settleStrategy;

    private SettleHelper(){
        settleStrategy = new NoneSettleStrategy();
    }

    public SettleHelper setSettleStrategy(SettleStrategy settleStrategy) {
        this.settleStrategy = settleStrategy;
        return this;
    }

    public List<SettleItemDTO> settle(List<SettleItemDTO> itemsToSettle) {
        return settleStrategy.simplifySettleItems(itemsToSettle);
    }
}
