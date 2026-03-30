package com.bank.account_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor   // ✅ REQUIRED for searialization
@AllArgsConstructor
@Builder
public class AccountResponse {

    private String accountNumber;
    private Long userId;
    private Double balance;
}