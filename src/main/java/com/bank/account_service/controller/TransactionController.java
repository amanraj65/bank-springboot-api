package com.bank.account_service.controller;

import com.bank.account_service.dto.AccountResponse;
import com.bank.account_service.dto.TransferRequest;
import com.bank.account_service.dto.TransferResponse;
import com.bank.account_service.service.IdempotencyService;
import com.bank.account_service.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class TransactionController {

    private final TransactionService transactionService;
    private final IdempotencyService idempotencyService;

    @PostMapping("/transfer")
    public TransferResponse transferMoney(
            @RequestHeader("Idempotency-Key") String key,
            @Valid @RequestBody TransferRequest request
    ) {
        return idempotencyService.process(key,
                () -> transactionService.transferMoney(request),
                TransferResponse.class
        );
    }
}