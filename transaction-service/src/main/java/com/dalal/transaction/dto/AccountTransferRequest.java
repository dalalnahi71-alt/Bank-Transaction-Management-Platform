package com.dalal.transaction.dto;
import java.math.BigDecimal;
public record AccountTransferRequest(Long sourceAccountId, Long destinationAccountId, BigDecimal amount) { }
