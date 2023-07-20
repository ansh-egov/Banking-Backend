package com.example.Banking.Server.BankingServer.services;

import com.example.Banking.Server.BankingServer.Mapper.BankAccountRowMapper;
import com.example.Banking.Server.BankingServer.models.AccountTransactions;
import com.example.Banking.Server.BankingServer.models.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.*;

@Service
public class BankAccountServices {
    JdbcTemplate jdbcTemplate;
    private Set<String> generatedNumbers;
    private Random random;

    @Autowired
    public BankAccountServices(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
        generatedNumbers = new HashSet<>();
        random = new Random();
    }

    @PostConstruct
    public void createTable(){
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS BANK(" +
                "id UUID DEFAULT uuid_generate_v4() PRIMARY KEY," +
                "name VARCHAR(255), " +
                "accountNumber VARCHAR(255)," +
                "balance DECIMAL," +
                "UNIQUE (accountNumber)" +
                ");");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS TRANSACTIONS(" +
                "id UUID DEFAULT uuid_generate_v4() PRIMARY KEY," +
                "fromAccountNumber VARCHAR(255)," +
                "toAccountNumber VARCHAR(255)," +
                "amount DECIMAL," +
                "status VARCHAR(255)," +
                "FOREIGN KEY (fromAccountNumber) REFERENCES BANK (accountNumber)," +
                "FOREIGN KEY (toAccountNumber) REFERENCES BANK (accountNumber)" +
                ");");
    }

    private String generateUniqueNumberString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10);
            sb.append(digit);
        }

        String randomNumberString = sb.toString();
        while (generatedNumbers.contains(randomNumberString) && findByAccountNumber(randomNumberString) != null) {
            sb.setLength(0);
            for (int i = 0; i < length; i++) {
                int digit = random.nextInt(10);
                sb.append(digit);
            }
            randomNumberString = sb.toString();
        }

        generatedNumbers.add(randomNumberString);
        return randomNumberString;
    }

    public BankAccount createAccount(BankAccount bankAccount) throws Exception {
        String accountNumber = generateUniqueNumberString(12);
        bankAccount.setAccountNumber(accountNumber);
        String sql = "INSERT INTO BANK (name,accountNumber, balance) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, bankAccount.getName(), accountNumber, bankAccount.getBalance());
        return bankAccount;
    }

    public Void insertTransaction(AccountTransactions accountTransactions){
        String sql = "INSERT INTO TRANSACTIONS (fromAccountNumber,toAccountNumber, amount, status) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, accountTransactions.getFromAccountNumber(), accountTransactions.getToAccountNumber(), accountTransactions.getAmount(), accountTransactions.getStatus());
        return null;
    }

    public BankAccount findByAccountNumber(String accountNumber){
        String sql = "SELECT * FROM BANK WHERE accountNumber = ?;";
        return jdbcTemplate.queryForObject(sql,new BankAccountRowMapper(),accountNumber);
    }

    public BankAccount update(BankAccount bankAccount){
        StringBuilder sql = new StringBuilder("UPDATE BANK SET");
        List<Object> params = new ArrayList<>();

        if(bankAccount.getName() != null){
            sql.append(" name = ?,");
            params.add(bankAccount.getName());
        }
        if(bankAccount.getBalance() != null){
            sql.append(" balance = ?,");
            params.add(bankAccount.getBalance());
        }
        sql.setLength(sql.length() - 1);
        sql.append(" WHERE accountNumber = ?");
        params.add(bankAccount.getAccountNumber());

        jdbcTemplate.update(sql.toString(),params.toArray());
        return bankAccount;
    }

    public Object getTransactions(String accountNumber) {
        String sql = "select transactions.fromAccountNumber,transactions.toAccountNumber,transactions.amount,transactions.status from bank INNER JOIN transactions on bank.accountNumber = transactions.fromAccountNumber;";
        return jdbcTemplate.queryForList(sql);
    }
}
