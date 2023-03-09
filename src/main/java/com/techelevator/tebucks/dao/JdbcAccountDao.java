package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.math.BigDecimal;


public class JdbcAccountDao implements AccountDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account getAccountById(long id) {
        String sql = "SELECT * FROM accounts WHERE account_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if (results.next()) {
            return mapRowToAccount(results);
        } else {
            return null;
        }
    }

    @Override
    public boolean update(long id, Account account) {
        String sql = "UPDATE account SET balance = ? WHERE id = ? ";
        return jdbcTemplate.update(sql, account.getBalance(), id) == 1;
    }

    @Override
    public BigDecimal getBalance(long userId) {
        String sql = "Select balance FROM accounts WHERE account_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()) {
            return mapRowToAccount(results).getBalance();
        }
        return null;
    }

    @Override
    public boolean createAccount(long userId) {
        String sql = "INSERT into accounts (account_id, user_id, balance, active)" +
                " VALUES (DEFAULT, ?, DEFAULT, DEFAULT) RETURNING account_id";
        Integer newAccountId;
        newAccountId =  jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return newAccountId != null;
    }

    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setId(rs.getInt("account_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        return account;
    }

}
