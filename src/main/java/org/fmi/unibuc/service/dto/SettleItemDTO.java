package org.fmi.unibuc.service.dto;

import java.math.BigDecimal;

public class SettleItemDTO {

    private long fromAppUserId;
    private String fromAppUserFirstName;
    private String fromAppUserLastName;

    private long toAppUserId;
    private String toAppUserFirstName;
    private String toAppUserLastName;

    private BigDecimal amount;

    public SettleItemDTO(long fromAppUserId, String fromAppUserFirstName, String fromAppUserLastName, BigDecimal amount) {
        this.fromAppUserId = fromAppUserId;
        this.fromAppUserFirstName = fromAppUserFirstName;
        this.fromAppUserLastName = fromAppUserLastName;
        this.amount = amount;
    }

    public SettleItemDTO(long fromAppUserId, String fromAppUserFirstName, String fromAppUserLastName, long toAppUserId, String toAppUserFirstName, String toAppUserLastName, BigDecimal amount) {
        this.fromAppUserId = fromAppUserId;
        this.fromAppUserFirstName = fromAppUserFirstName;
        this.fromAppUserLastName = fromAppUserLastName;
        this.toAppUserId = toAppUserId;
        this.toAppUserFirstName = toAppUserFirstName;
        this.toAppUserLastName = toAppUserLastName;
        this.amount = amount;
    }

    public long getFromAppUserId() {
        return fromAppUserId;
    }

    public void setFromAppUserId(long fromAppUserId) {
        this.fromAppUserId = fromAppUserId;
    }

    public String getFromAppUserFirstName() {
        return fromAppUserFirstName;
    }

    public void setFromAppUserFirstName(String fromAppUserFirstName) {
        this.fromAppUserFirstName = fromAppUserFirstName;
    }

    public String getFromAppUserLastName() {
        return fromAppUserLastName;
    }

    public void setFromAppUserLastName(String fromAppUserLastName) {
        this.fromAppUserLastName = fromAppUserLastName;
    }

    public long getToAppUserId() {
        return toAppUserId;
    }

    public void setToAppUserId(long toAppUserId) {
        this.toAppUserId = toAppUserId;
    }

    public String getToAppUserFirstName() {
        return toAppUserFirstName;
    }

    public void setToAppUserFirstName(String toAppUserFirstName) {
        this.toAppUserFirstName = toAppUserFirstName;
    }

    public String getToAppUserLastName() {
        return toAppUserLastName;
    }

    public void setToAppUserLastName(String toAppUserLastName) {
        this.toAppUserLastName = toAppUserLastName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
