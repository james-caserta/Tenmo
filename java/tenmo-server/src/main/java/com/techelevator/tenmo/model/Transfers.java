package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfers {

    private int transferId;
    private String transferType;
    private String transferStatus;
    private int accountIdFrom;
    private String accountNameFrom;
    private int accountIdTo;
    private String accountNameTo;
    private BigDecimal amount;

    public Transfers(int transferId, String transferType, String transferStatus, int accountIdFrom, String accountNameFrom, int accountIdTo, String accountNameTo, BigDecimal amount) {
        this.transferId = transferId;
        this.transferType = transferType;
        this.transferStatus = transferStatus;
        this.accountIdFrom = accountIdFrom;
        this.accountNameFrom = accountNameFrom;
        this.accountIdTo = accountIdTo;
        this.accountNameTo = accountNameTo;
        this.amount = amount;
    }

    public Transfers() {
        //empty constructor
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public int getAccountIdFrom() {
        return accountIdFrom;
    }

    public void setAccountIdFrom(int accountIdFrom) {
        this.accountIdFrom = accountIdFrom;
    }

    public String getAccountNameFrom() {
        return accountNameFrom;
    }

    public void setAccountNameFrom(String accountNameFrom) {
        this.accountNameFrom = accountNameFrom;
    }

    public int getAccountIdTo() {
        return accountIdTo;
    }

    public void setAccountIdTo(int accountIdTo) {
        this.accountIdTo = accountIdTo;
    }

    public String getAccountNameTo() {
        return accountNameTo;
    }

    public void setAccountNameTo(String accountNameTo) {
        this.accountNameTo = accountNameTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Transfers{" +
                "transferType='" + transferType + '\'' +
                ", transferStatus='" + transferStatus + '\'' +
                ", accountIdFrom=" + accountIdFrom +
                ", accountNameFrom='" + accountNameFrom + '\'' +
                ", accountIdTo=" + accountIdTo +
                ", accountNameTo='" + accountNameTo + '\'' +
                ", amount=" + amount +
                '}';
    }
}
