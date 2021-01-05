package org.fmi.unibuc.service.reporting.settlestrategy;

import org.fmi.unibuc.service.dto.SettleItemDTO;

import java.util.List;

/*
 * Use case example:
 * A --60--> B,C, B --25--> A, C--30--> B
 *
 * NONE - the settle items should not be altered at all
 *        For the above example, the settleItemList should be:
 *        B owns 30 to A
 *        C owns 30 to A
 *        A owns 25 to B
 *        B owns 30 to C
 * SIMPLE - the settle should be made just peer-to-peer
 *          For the above example, the settleItemList should be
 *           B owns 5 to A
 *           C owns 30 to A
 *           B owns 30 to C
 * COMPLEX - the settle should be made such that there are as few transactions as possible
 *           B owns 35 to A
 * */
public interface SettleStrategy {

    List<SettleItemDTO> simplifySettleItems(List<SettleItemDTO> settleItemDTOList);

}
