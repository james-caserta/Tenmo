package com.techelevator.tenmo.dao;

import java.math.BigDecimal;


import com.techelevator.tenmo.model.Accounts;

public interface AccountsDao {

    BigDecimal viewCurrentBalance(long userId);

    BigDecimal creditBalance(Accounts account, BigDecimal amountToAdd);

    BigDecimal deductBalance(Accounts account, BigDecimal amountToSubtract);

    Accounts findAccountByUserId(long userId);

}
