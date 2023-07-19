package com.example.Banking.Server.BankingServer.Controller;

import com.example.Banking.Server.BankingServer.Producer.BankAccountProducer;
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

    @RequestMapping(value = "/_debit/{accountNumber}",method = RequestMethod.PATCH)
    public ResponseEntity<String> debitAmount(@PathVariable String accountNumber, @RequestBody BigDecimal debitAmount){
        if(bankAccountServices.findByAccountNumber(accountNumber) == null){
            return new ResponseEntity<>("Bank Account with accountNumber: " + accountNumber + " does not exist",HttpStatus.NOT_FOUND);
        }
        bankAccountProducer.debitAmountByKafka(accountNumber,debitAmount,"Debit");
        return new ResponseEntity<>("Successfully debited the amount from bank account with accountNumber: " + accountNumber,HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/_credit/{accountNumber}",method = RequestMethod.PATCH)
    public ResponseEntity<String> creditAmount(@PathVariable String accountNumber, @RequestBody BigDecimal creditAmount){
        if(bankAccountServices.findByAccountNumber(accountNumber) == null){
            return new ResponseEntity<>("Bank Account with accountNumber: " + accountNumber + " does not exist",HttpStatus.NOT_FOUND);
        }
        bankAccountProducer.creditAmountByKafka(accountNumber,creditAmount,"Credit");
        return new ResponseEntity<>("Successfully credited the amount from bank account with accountNumber: " + accountNumber,HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/_transactions/{accountNumber}",method = RequestMethod.GET)
    public ResponseEntity<Object> getTransactions(@PathVariable String accountNumber){
        return new ResponseEntity<Object>(bankAccountServices.getTransactions(accountNumber),HttpStatus.OK);
    }
}
