package org.fmi.unibuc.service.reporting.settlestrategy;

import org.fmi.unibuc.service.reporting.SettleLevel;

public class SettleStrategyFactory {

    public static final SettleStrategyFactory INSTANCE = new SettleStrategyFactory();

    private SettleStrategyFactory() {

    }

    public SettleStrategy getSettleStrategy(SettleLevel settleLevel) {
        switch (settleLevel) {
            case NONE:
                return new NoneSettleStrategy();
            case SIMPLE:
                return new SimpleSettleStrategy();
            case COMPLEX:
                return new ComplexSettleStrategy();
        }
        return new NoneSettleStrategy();
    }

}
