package com.example.Banking.Server.BankingServer.Mapper;

import com.example.Banking.Server.BankingServer.models.BankAccount;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class BankAccountRowMapper implements RowMapper<BankAccount> {
    @Override
    public BankAccount mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        BankAccount account = new BankAccount();
        account.setId(UUID.fromString(resultSet.getString("id")));
        account.setAccountNumber(resultSet.getString("accountNumber"));
        account.setBalance(resultSet.getBigDecimal("balance"));
        account.setName(resultSet.getString("name"));
        return account;
    }
}
