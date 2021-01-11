package org.fmi.unibuc.service.reporting.settlestrategy;

import org.fmi.unibuc.service.dto.SettleItemDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SimpleSettleStrategy implements SettleStrategy {
    @Override
    public List<SettleItemDTO> simplifySettleItems(List<SettleItemDTO> settleItemDTOList) {
        List<SettleItemDTO> outSettleItemDTOList = new ArrayList<>();
        return outSettleItemDTOList;
    }

    private List<SettleItemDTO> simpleSettle(List<SettleItemDTO> inputList) {

        //Map<Edge>

        return new ArrayList<SettleItemDTO>();

    }

    protected static class Edge {

        Node from;
        Node to;
        BigDecimal amount;
    }

    protected static class Node {
        public String firstName;
        public String lastName;
        public Long appUserId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return appUserId.equals(node.appUserId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(appUserId);
        }
    }
}
