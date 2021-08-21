package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Accounts {
    private int accountId;
    private int userId;
    private BigDecimal accountBalance = new BigDecimal("0.00");


    public Accounts(int accountId, int userId, BigDecimal accountBalance) {
        this.accountId = accountId;
        this.userId = userId;
        this.accountBalance = accountBalance;
    }

    public Accounts() {

    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    @Override
    public String toString() {
        return "Accounts{" +
                "accountId=" + accountId +
                ", userId=" + userId +
                ", accountBalance=" + accountBalance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || this.getClass() != o.getClass())
            return false;
        Accounts account = (Accounts)o;
        return (this.getUserId() == account.getUserId() && this.getAccountId() == account.getAccountId()
                && this.getAccountBalance().compareTo(account.getAccountBalance()) == 0);
    }

    // Check sufficient balance from payer account
    public boolean hasEnoughMoney(BigDecimal amount) {
        return (this.getAccountBalance().compareTo(amount) >= 0);
    }
}
