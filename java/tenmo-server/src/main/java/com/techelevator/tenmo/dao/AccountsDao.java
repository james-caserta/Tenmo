package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Accounts;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public interface AccountsDao {

    BigDecimal viewCurrentBalance(int userId);

    BigDecimal creditBalance(Accounts account, BigDecimal amountToAdd);
    BigDecimal deductBalance(Accounts account, BigDecimal amountToSubtract);

    Accounts findAccountByUserId(int userId);
}
