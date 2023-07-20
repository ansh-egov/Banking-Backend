package com.example.Banking.Server.BankingServer.Consumer;

import com.example.Banking.Server.BankingServer.models.AccountTransactions;
import com.example.Banking.Server.BankingServer.models.BankAccount;
import com.example.Banking.Server.BankingServer.services.BankAccountServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.BitSet;

@Component
public class BankAccountConsumer {
    ObjectMapper objectMapper;
    BankAccountServices bankAccountServices;

    @Autowired
    public BankAccountConsumer(ObjectMapper objectMapper, BankAccountServices bankAccountServices) {
        this.objectMapper = objectMapper;
        this.bankAccountServices = bankAccountServices;
    }

    @KafkaListener(topics = "${kafka.topic.bank-account-transfer}")
    public void transferAmount(String message) throws JsonProcessingException {
        AccountTransactions accountTransactions = objectMapper.readValue(message,AccountTransactions.class);
        BankAccount bankAccountToDebitFrom = bankAccountServices.findByAccountNumber(accountTransactions.getFromAccountNumber());
        BankAccount bankAccountToCreditFor = bankAccountServices.findByAccountNumber(accountTransactions.getToAccountNumber());
        BigDecimal amountToTransfer = accountTransactions.getAmount();
        bankAccountToDebitFrom.setBalance(bankAccountToDebitFrom.getBalance().subtract(amountToTransfer));
        bankAccountToCreditFor.setBalance(bankAccountToCreditFor.getBalance().add(amountToTransfer));
        bankAccountServices.insertTransaction(accountTransactions);
        bankAccountServices.insertTransaction(new AccountTransactions(accountTransactions.getToAccountNumber(), accountTransactions.getFromAccountNumber(), accountTransactions.getAmount(),"Credit"));
        bankAccountServices.update(bankAccountToCreditFor);
        bankAccountServices.update(bankAccountToDebitFrom);
    }

//    @KafkaListener(topics = "${kafka.topic.bank-account-credit}")
//    public void creditAmount(String message) throws JsonProcessingException {
//        AccountTransactions accountTransactions = objectMapper.readValue(message,AccountTransactions.class);
//        BankAccount bankAccount = bankAccountServices.findByAccountNumber(accountTransactions.getAccountNumber());
//        BigDecimal currentBalance = bankAccount.getBalance();
//        BigDecimal amountCredit = accountTransactions.getAmount();
//        BigDecimal amount = currentBalance.add(amountCredit);
//        bankAccountServices.insertTransaction(accountTransactions.getAccountNumber(),amountCredit, accountTransactions.getStatus());
//        System.out.println(accountTransactions);
//        bankAccount.setBalance(amount);
//        bankAccountServices.update(bankAccount);
//    }
}
