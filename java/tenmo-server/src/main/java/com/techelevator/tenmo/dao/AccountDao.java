package com.techelevator.tenmo.dao;

import java.math.BigDecimal;


import com.techelevator.tenmo.model.Account;

public interface AccountDao {


    //*************     viewCurrentBalance issue  ??     **************
    BigDecimal viewCurrentBalance(long userId);

    BigDecimal creditBalance(Account account, BigDecimal amountToAdd);

    BigDecimal deductBalance(Account account, BigDecimal amountToSubtract);

    Account findAccountByUserId(long userId);

}