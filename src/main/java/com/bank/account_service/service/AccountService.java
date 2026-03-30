package com.bank.account_service.service;

import com.bank.account_service.dto.AccountRequest;
import com.bank.account_service.dto.AccountResponse;
import com.bank.account_service.model.Account;
import com.bank.account_service.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountNumberGenerator accountNumberGenerator;

    public AccountResponse createAccount(AccountRequest request){
        Account account = Account.builder()
                .userId(request.getUserId())
                .balance(request.getBalance())
                .accountNumber(accountNumberGenerator.generateAccountNumber())
                .build();

        Account saved = accountRepository.save(account);
        return AccountResponse.builder()
                .accountNumber(saved.getAccountNumber())
                .balance(saved.getBalance())
                .build();

    }

    @Cacheable(value = "accounts", key = "#accountNumber")
    public AccountResponse getAccount(String accountNumber){
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new RuntimeException("Account Not Found"));

        return AccountResponse.builder()
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .build();
    }

}