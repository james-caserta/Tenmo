package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;


@Component
public class JdbcAccountDao implements AccountDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    //*************     viewCurrentBalance issue ?? says never used in DAO   **************
    @Override
    public BigDecimal viewCurrentBalance(long userId) {
        String sql = "SELECT * "
                + "FROM accounts "
                + "WHERE user_id = ? ";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        BigDecimal balance = null;
//      BigDecimal balance = new BigDecimal(userId);
        if (results.next()) {
            balance = results.getBigDecimal("balance");
        }
        return balance;
    }

    @Override
    public BigDecimal creditBalance(Account accountInfo, BigDecimal amountToAdd) {
        // Updates database - addition
        String addMoneyToDB = "UPDATE accounts SET balance = ? WHERE user_id = ?";
        jdbcTemplate.update(addMoneyToDB, accountInfo.getAccountBalance().add(amountToAdd),
                accountInfo.getUserId());
        // Updates object
        accountInfo.setAccountBalance(accountInfo.getAccountBalance().add(amountToAdd));
        return accountInfo.getAccountBalance();
    }

    @Override
    public BigDecimal deductBalance(Account subtractAccount, BigDecimal amountToSubtract) {
        if (subtractAccount.correctMoney(amountToSubtract)) {
            // Updates database - subtract
            String subtractFromSql = "UPDATE accounts SET balance = ? WHERE user_id = ?";
            jdbcTemplate.update(subtractFromSql, subtractAccount.getAccountBalance().subtract(amountToSubtract),
                    subtractAccount.getUserId());
            // Updates object
            subtractAccount.setAccountBalance(subtractAccount.getAccountBalance().subtract(amountToSubtract));
        }
        return subtractAccount.getAccountBalance();
    }

    @Override
    public Account findAccountByUserId(long userId) {
        Account account = null;
        String sql = "SELECT * FROM accounts WHERE user_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId);
        if (result.next()) {
            account = mapRowToAccount(result);
        }
        return account;
    }


    private Account mapRowToAccount(SqlRowSet results) {
        Account account = new Account();
        account.setAccountBalance(results.getBigDecimal("balance"));
        account.setAccountId(results.getLong("account_id"));
        account.setUserId(results.getLong("user_id"));
        return account;
    }
}