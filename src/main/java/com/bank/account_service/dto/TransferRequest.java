package com.bank.account_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferRequest {
    @NotBlank(message = "From account number is required")
    @Pattern(regexp = "^ACC\\d{8}$", message = "From account must be like ACC12345678")
    private String fromAccount;

    @Pattern(regexp = "^ACC\\d{8}$", message = "To account must be like ACC12345678")
    @NotBlank(message = "To account number is required")
    private String toAccount;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than 0")
    private Double amount;

}
