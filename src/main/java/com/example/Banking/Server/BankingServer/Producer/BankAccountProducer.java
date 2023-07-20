package com.example.Banking.Server.BankingServer.Producer;

import com.example.Banking.Server.BankingServer.models.AccountTransactions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BankAccountProducer {
    private final KafkaTemplate<String, AccountTransactions> kafkaTemplate;

    @Value("${kafka.topic.bank-account-transfer}")
    private String bankAccountTransferTopic;

    @Autowired
    public BankAccountProducer(KafkaTemplate<String,AccountTransactions> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    public void debitAmountByKafka(AccountTransactions accountTransactions){
        kafkaTemplate.send(bankAccountTransferTopic,accountTransactions);
    }
}
