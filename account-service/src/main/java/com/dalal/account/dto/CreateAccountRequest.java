package com.dalal.account.dto;

import com.dalal.account.entity.AccountType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import java.math.BigDecimal;

public record CreateAccountRequest(
        @NotNull(message = "Customer ID is required") @Positive(message = "Customer ID must be positive") Integer customerId,
        @NotNull(message = "Account type is required") AccountType accountType,
        @NotNull(message = "Initial balance is required")
        @DecimalMin(value = "0.00", message = "Initial balance cannot be negative")
        @Digits(integer = 13, fraction = 2, message = "Balance supports up to 2 decimal places") BigDecimal initialBalance
) { }
