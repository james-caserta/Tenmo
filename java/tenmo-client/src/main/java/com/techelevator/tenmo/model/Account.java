package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {
    private Long accountId;
    private Long userId;
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

//package com.techelevator.tenmo.model;
//
//public class Accounts {
//    private Long accountId;
//    private int userId;
//    private double balance;
//
//
//    public Accounts() {
//        this.accountId = accountId;
//        this.userId = userId;
//        this.balance = balance;
//    }
//
//    public Long getAccountId() { return accountId; }
//
//    public void setAccountId(Long accountId) { this.accountId = accountId; }
//
//    public int getUserId() { return userId; }
//
//    public void setUserId(int userId) { this.userId = userId; }
//
//    public double getBalance() { return balance; }
//
//    public void setBalance(double balance) { this.balance = balance; }
//
//
//    @Override
//    public String toString() {
//        return "Accounts{" +
//                "account_id=" + accountId +
//                ", user_id=" + userId +
//                ", balance=" + balance +
//                '}';
//    }
//}