package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfers {

    private int transferId;
    private String transferType;
    private String transferStatus;
    private int accountIdFrom;
    private int accountIdTo;
    private String accountNameFrom;
    private String accountNameTo;
    private BigDecimal amount;

    public Transfers(int transferId, String transferType, String transferStatus, int accountFrom, int accountTo, String accountNameFrom, String accountNameTo, BigDecimal amount) {
        this.transferId = transferId;
        this.transferType = transferType;
        this.transferStatus = transferStatus;
        this.accountIdFrom = accountFrom;
        this.accountIdTo = accountTo;
        this.accountNameFrom = accountNameFrom;
        this.accountNameTo = accountNameTo;
        this.amount = amount;
    }

    public Transfers(String transferTypeSend, String transferStatusApproved, Integer id, long sendToChoice, BigDecimal amount) {
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

    public int getAccountIdTo() {
        return accountIdTo;
    }

    public void setAccountIdTo(int accountIdTo) {
        this.accountIdTo = accountIdTo;
    }

    public String getAccountNameFrom() {
        return accountNameFrom;
    }

    public void setAccountNameFrom(String accountNameFrom) {
        this.accountNameFrom = accountNameFrom;
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
                "transferId=" + transferId +
                ", transferType='" + transferType + '\'' +
                ", transferStatus='" + transferStatus + '\'' +
                ", accountFrom=" + accountIdFrom +
                ", accountTo=" + accountIdTo +
                ", accountNameFrom='" + accountNameFrom + '\'' +
                ", accountNameTo='" + accountNameTo + '\'' +
                ", amount=" + amount +
                '}';
    }
}
