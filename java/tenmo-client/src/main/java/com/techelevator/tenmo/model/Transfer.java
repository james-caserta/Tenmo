package com.techelevator.tenmo.models;

import java.math.BigDecimal;

// Client-side Transfer
public class Transfer {
    private String transferType;
    private String transferStatus;
    private long accountFromId;
    private String accountFromName;
    private long accountToId;
    private String accountToName;
    private BigDecimal amount;
    private long transferId;

    public Transfer(String transferType, String transferStatus, long accountFromId, String accountFromName,
                    long accountToId, String accountToName, BigDecimal amount, long transferId) {

        this.transferType = transferType;
        this.transferStatus = transferStatus;
        this.accountFromId = accountFromId;
        this.accountFromName = accountFromName;
        this.accountToId = accountToId;
        this.accountToName = accountToName;
        this.amount = amount;
        this.transferId = transferId;
    }
    public Transfer(String transferType, String transferStatus, long accountFromId, long accountToId,
                    BigDecimal amount) {

        this.transferType = transferType;
        this.transferStatus = transferStatus;
        this.accountFromId = accountFromId;
        this.accountToId = accountToId;
        this.amount = amount;

    }
    public Transfer() {

    }

    public String getAccountFromName() {
        return accountFromName;
    }
    public void setAccountFromName(String accountFromName) {
        this.accountFromName = accountFromName;
    }
    public String getAccountToName() {
        return accountToName;
    }
    public void setAccountToName(String accountToName) {
        this.accountToName = accountToName;
    }


    public long getTransferId() {
        return transferId;
    }

    public void setTransferId(long transferId) {
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

    public long getAccountFromId() {
        return accountFromId;
    }

    public void setAccountFromId(long accountFromId) {
        this.accountFromId = accountFromId;
    }

    public long getAccountToId() {
        return accountToId;
    }

    public void setAccountToId(long accountToId) {
        this.accountToId = accountToId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}
