package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {
    private Long accountId;
    public Long userId;
    private BigDecimal balance;

    public Account(Long accountId, Long userId, BigDecimal balance) {
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
    }

    public Account() {

    }



    public Long getAccountId() { return accountId; }

    public void setAccountId(Long accountId) { this.accountId = accountId; }

    public Long getUserId() { return userId; }

    public void setUserId(Long userId) { this.userId = userId; }

    public BigDecimal getBalance() { return balance; }

    public void setBalance(BigDecimal balance) { this.balance = balance; }


    @Override
    public String toString() {
        return "Accounts{" +
                "account_id=" + accountId +
                ", user_id=" + userId +
                ", balance=" + balance +
                '}';
    }
}
