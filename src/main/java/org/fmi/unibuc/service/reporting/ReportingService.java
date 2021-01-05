package org.fmi.unibuc.service.reporting;

import org.fmi.unibuc.service.dto.SettleItemDTO;

import java.util.List;

public interface ReportingService {

    List<SettleItemDTO> getSettleItems(SettleLevel settleLevel);

}
