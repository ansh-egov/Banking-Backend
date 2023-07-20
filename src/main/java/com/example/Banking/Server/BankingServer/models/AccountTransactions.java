package com.example.Banking.Server.BankingServer.models;

import java.math.BigDecimal;
import java.util.UUID;

public class AccountTransactions {

    private UUID id;
    private String fromAccountNumber;
    private String toAccountNumber;
    private BigDecimal amount;
    private String status;

    public AccountTransactions() {
    }

    public AccountTransactions(UUID id, String fromAccountNumber, String toAccountNumber, BigDecimal amount, String status) {
        this.id = id;
        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
        this.amount = amount;
        this.status = status;
    }

    public AccountTransactions(String fromAccountNumber, String toAccountNumber, BigDecimal amount, String status) {
        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
        this.amount = amount;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFromAccountNumber() {
        return fromAccountNumber;
    }

    public void setFromAccountNumber(String fromAccountNumber) {
        this.fromAccountNumber = fromAccountNumber;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }

    public void setToAccountNumber(String toAccountNumber) {
        this.toAccountNumber = toAccountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

