package com.dalal.transaction.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

public record MoneyTransactionRequest(
        @NotNull(message = "Account ID is required") Long accountId,
        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
        @Digits(integer = 13, fraction = 2, message = "Amount supports up to 2 decimal places") BigDecimal amount,
        @NotBlank(message = "Description is required") String description
) { }
