package com.example.Banking.Server.BankingServer.Controller;

import com.example.Banking.Server.BankingServer.Producer.BankAccountProducer;
import com.example.Banking.Server.BankingServer.models.AccountTransactions;
import com.example.Banking.Server.BankingServer.models.BankAccount;
import com.example.Banking.Server.BankingServer.services.BankAccountServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@RestController
@RequestMapping("/account")
public class BankAccountController {
    private BankAccountServices bankAccountServices;
    private BankAccountProducer bankAccountProducer;

    @Autowired
    public BankAccountController(BankAccountServices bankAccountServices,BankAccountProducer bankAccountProducer){
        this.bankAccountServices = bankAccountServices;
        this.bankAccountProducer = bankAccountProducer;
    }

    @RequestMapping(value = "/_create",method = RequestMethod.POST)
    public ResponseEntity<String> createBankAccount(@RequestBody BankAccount bankAccount) throws Exception {
        bankAccountServices.createAccount(bankAccount);
        return new ResponseEntity<String>("Created Bank Account with account No.: " + bankAccount.getAccountNumber(),
        HttpStatus.CREATED);
    }
    @RequestMapping(value = "/_transfer",method = RequestMethod.PATCH)
    public ResponseEntity<String> transferAmountFromOneBankAccountToAnother(@RequestBody AccountTransactions accountTransactions){
        if(bankAccountServices.findByAccountNumber(accountTransactions.getFromAccountNumber()) == null){
            return new ResponseEntity<>("Bank Account with accountNumber: " + accountTransactions.getFromAccountNumber() + " does not exist",HttpStatus.NOT_FOUND);
        }
        if(bankAccountServices.findByAccountNumber(accountTransactions.getToAccountNumber()) == null){
            return new ResponseEntity<>("Bank Account with accountNumber: " + accountTransactions.getToAccountNumber() + " does not exist",HttpStatus.NOT_FOUND);
        }
        BankAccount bankAccount = bankAccountServices.findByAccountNumber(accountTransactions.getFromAccountNumber());
        if(bankAccount.getBalance().compareTo(accountTransactions.getAmount()) < 0){
            return new ResponseEntity<>("Insufficient Balance", HttpStatus.NOT_ACCEPTABLE);
        }
        bankAccountProducer.debitAmountByKafka(accountTransactions);
        return new ResponseEntity<>("Transferred",HttpStatus.ACCEPTED);
    }
    @RequestMapping(value = "/_transactions/{accountNumber}",method = RequestMethod.GET)
    public ResponseEntity<Object> getTransactions(@PathVariable String accountNumber){
        return new ResponseEntity<Object>(bankAccountServices.getTransactions(accountNumber),HttpStatus.OK);
    }
}
