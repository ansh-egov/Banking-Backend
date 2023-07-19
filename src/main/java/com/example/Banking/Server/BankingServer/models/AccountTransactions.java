package com.example.Banking.Server.BankingServer.models;

import java.math.BigDecimal;
import java.util.UUID;

public class AccountTransactions {

    private UUID id;
    private String accountNumber;
    private BigDecimal amount;
    private String status;

    public AccountTransactions() {
    }
    public AccountTransactions(String accountNumber, BigDecimal amount) {
        this.accountNumber = accountNumber;
        this.amount = amount;
    }

    public AccountTransactions(UUID id, String accountNumber, BigDecimal amount, String status) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.status = status;
    }

    public AccountTransactions(String accountNumber, BigDecimal amount, String status) {
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.status = status;
    }

    public AccountTransactions(UUID id, String accountNumber, BigDecimal amount) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.amount = amount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
