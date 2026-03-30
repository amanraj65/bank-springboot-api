package com.bank.account_service.controller;

import com.bank.account_service.dto.AccountRequest;
import com.bank.account_service.dto.AccountResponse;
import com.bank.account_service.model.Account;
import com.bank.account_service.model.IdempotencyKey;
import com.bank.account_service.service.AccountService;
import com.bank.account_service.service.IdempotencyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AccountController {

    private final AccountService accountService;
    private final IdempotencyService idempotencyService;

    @PostMapping
    public AccountResponse createAccount(
            @RequestHeader("Idempotency-Key") String key,
            @Valid @RequestBody AccountRequest request) {

        return idempotencyService.process(key,
                () -> accountService.createAccount(request),
                AccountResponse.class
        );
    }

    @GetMapping("/{accountNumber}")
    public AccountResponse getAccount(@PathVariable String accountNumber) {
        return accountService.getAccount(accountNumber);
    }

}