package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

import com.techelevator.tenmo.model.Accounts;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;


@Component
public class JdbcAccountsDao implements AccountsDao {
    private JdbcTemplate jdbcTemplate;

    public JdbcAccountsDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal viewCurrentBalance(long userId) {
        String sql = "SELECT * "
                + "FROM accounts "
                + "WHERE user_id = ? ";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        BigDecimal balance = null;
        if (results.next()) {
            balance = results.getBigDecimal("balance");
        }
        return balance;
    }

    @Override
    public BigDecimal creditBalance(Accounts accountInfo, BigDecimal amountToAdd) {
        // Updates database
        String addMoneyToDB = "UPDATE accounts SET balance = ? WHERE user_id = ?";
        jdbcTemplate.update(addMoneyToDB, accountInfo.getAccountBalance().add(amountToAdd),
                accountInfo.getUserId());
        // Updates Java object
        accountInfo.setAccountBalance(accountInfo.getAccountBalance().add(amountToAdd));
        return accountInfo.getAccountBalance();
    }

    @Override
    public BigDecimal deductBalance(Accounts subtractAccount, BigDecimal amountToSubtract) {
        if (subtractAccount.correctMoney(amountToSubtract)) {
            // Updates database
            String subtractFromSql = "UPDATE accounts SET balance = ? WHERE user_id = ?";
            jdbcTemplate.update(subtractFromSql, subtractAccount.getAccountBalance().subtract(amountToSubtract),
                    subtractAccount.getUserId());
            // Updates Java object
            subtractAccount.setAccountBalance(subtractAccount.getAccountBalance().subtract(amountToSubtract));
        }
        return subtractAccount.getAccountBalance();
    }

    @Override
    public Accounts findAccountByUserId(long userId) {
        Accounts account = null;
        String sql = "SELECT * FROM accounts WHERE user_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId);
        if (result.next()) {
            account = mapRowToAccount(result);
        }
        return account;
    }


//	@Override
//	public Account findAccountByUserName(String name) {
//		Account account = null;
//		String sql = "SELECT * FROM accounts WHERE username = ?";
//		SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId);
//		if (result.next()) {
//			account = mapRowToAccount(result);
//		}
//		return account;
//	}

    private Accounts mapRowToAccount(SqlRowSet results) {
        Accounts account = new Accounts();
        account.setAccountBalance(results.getBigDecimal("balance"));
        account.setAccountId(results.getLong("account_id"));
        account.setUserId(results.getLong("user_id"));
        return account;
    }
}
