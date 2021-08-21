package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Accounts;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountsDao implements AccountsDao{

    private JdbcTemplate jdbcTemplate;
    public void AccountsDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal viewCurrentBalance(int userId) {
        String sql = "SELECT balance FROM accounts WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        BigDecimal balance = null;
        if (results.next()) {
            balance = results.getBigDecimal("balance");
        }
        return balance;
    }

    @Override
    public BigDecimal creditBalance(Accounts receiveAccount, BigDecimal amountToAdd) {

// Updates database
        String sqlAddMoney = "UPDATE accounts SET balance = ? WHERE user_id = ?";
        jdbcTemplate.update(sqlAddMoney, receiveAccount.getAccountBalance().add(amountToAdd),
                receiveAccount.getUserId());

//Updates java object
        receiveAccount.setAccountBalance(receiveAccount.getAccountBalance().add(amountToAdd));
        return receiveAccount.getAccountBalance();
    }

    @Override
    public BigDecimal deductBalance(Accounts payAccount, BigDecimal amountToSubtract) {
        if (payAccount.hasEnoughMoney(amountToSubtract)) {

            // Updates database
            String sqlDeductMoney = "UPDATE accounts SET balance = ? WHERE user_id = ?";
            jdbcTemplate.update(sqlDeductMoney, payAccount.getAccountBalance().subtract(amountToSubtract),
                    payAccount.getUserId());

            // Updates Java object
            payAccount.setAccountBalance(payAccount.getAccountBalance().subtract(amountToSubtract));
        }
        return payAccount.getAccountBalance();
    }

    @Override
    public Accounts findAccountByUserId(int userId) {
        Accounts account = null;
        String sql = "SELECT * FROM accounts WHERE user_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId);
        if (result.next()) {
            account = mapRowToAccount(result);
        }
        return account;
    }

    private Accounts mapRowToAccount(SqlRowSet results) {
        Accounts account = new Accounts();
        account.setAccountBalance(results.getBigDecimal("balance"));
        account.setAccountId(results.getInt("account_id"));
        account.setUserId(results.getInt("user_id"));
        return account;
    }
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
