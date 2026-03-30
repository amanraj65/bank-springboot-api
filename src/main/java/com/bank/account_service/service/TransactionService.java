package com.bank.account_service.service;

import com.bank.account_service.dto.TransferRequest;
import com.bank.account_service.dto.TransferResponse;
import com.bank.account_service.exception.AccountNotFoundException;
import com.bank.account_service.exception.InsufficientBalanceException;
import com.bank.account_service.model.Account;
import com.bank.account_service.model.Transaction;
import com.bank.account_service.repository.AccountRepository;
import com.bank.account_service.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionIdGenerator transactionIdGenerator;

    @Caching(evict = {
            @CacheEvict(value = "accounts", key = "#request.fromAccount"),
            @CacheEvict(value = "accounts", key = "#request.toAccount")
    })
    public TransferResponse transferMoney(TransferRequest request){

        //Fetching accounts of both from and to
        Account fromAcc = accountRepository.findByAccountNumber(request.getFromAccount())
                .orElseThrow(() -> new AccountNotFoundException("From Account number not found"));
        Account toAcc = accountRepository.findByAccountNumber(request.getToAccount())
                .orElseThrow(() -> new AccountNotFoundException("To Account number not found"));

        //When Account request balance is less than the account balance
        if (fromAcc.getBalance() < request.getAmount()){
            String txnId = transactionIdGenerator.generateTransactionId();
            Transaction failedTxn = Transaction.builder()
                    .fromAccount(request.getFromAccount())
                    .toAccount(request.getToAccount())
                    .amount(request.getAmount())
                    .transactionID(txnId)
                    .status("FAILED")
                    .createdAt(LocalDateTime.now())
                    .build();

            transactionRepository.save(failedTxn);

            //returning failed message
            throw new InsufficientBalanceException("Insufficient balance in sender account");
        }

        String txnId = transactionIdGenerator.generateTransactionId();
        //debit
        fromAcc.setBalance(fromAcc.getBalance() - request.getAmount());

        //credit
        toAcc.setBalance(toAcc.getBalance() + request.getAmount());

        accountRepository.save(fromAcc);
        accountRepository.save(toAcc);

        Transaction successTxn = Transaction.builder()
                .transactionID(txnId)
                .fromAccount(request.getFromAccount())
                .toAccount(request.getToAccount())
                .amount(request.getAmount())
                .status("SUCCESS")
                .createdAt(LocalDateTime.now())
                .build();

        transactionRepository.save(successTxn);

        return TransferResponse.builder()
                .transactionId(txnId)
                .status("SUCCESS")
                .message("Transfer Completed Successfully")
                .build();
    }
}
