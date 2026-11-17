package com.dalal.transaction.service;

import com.dalal.transaction.client.AccountServiceClient;
import com.dalal.transaction.dto.MoneyTransactionRequest;
import com.dalal.transaction.entity.BankTransaction;
import com.dalal.transaction.entity.TransactionStatus;
import com.dalal.transaction.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @Mock TransactionRepository repository;
    @Mock AccountServiceClient accountClient;
    @InjectMocks TransactionService service;

    @Test
    void successfulDepositIsRecordedAsCompleted() {
        when(repository.save(any(BankTransaction.class))).thenAnswer(invocation -> invocation.getArgument(0));
        BankTransaction result = service.deposit(new MoneyTransactionRequest(1L, new BigDecimal("50.00"), "Cash deposit"));
        assertEquals(TransactionStatus.COMPLETED, result.getStatus());
        verify(accountClient).deposit(eq(1L), any());
        verify(repository, times(2)).save(any(BankTransaction.class));
    }

    @Test
    void rejectedAccountOperationIsRecordedAsFailed() {
        when(repository.save(any(BankTransaction.class))).thenAnswer(invocation -> invocation.getArgument(0));
        doThrow(new org.springframework.web.client.RestClientException("account service unavailable"))
                .when(accountClient).deposit(eq(1L), any());

        assertThrows(com.dalal.transaction.exception.AccountOperationException.class,
                () -> service.deposit(new MoneyTransactionRequest(1L, new BigDecimal("50.00"), "Cash deposit")));

        org.mockito.ArgumentCaptor<BankTransaction> captor = org.mockito.ArgumentCaptor.forClass(BankTransaction.class);
        verify(repository, times(2)).save(captor.capture());
        assertEquals(TransactionStatus.FAILED, captor.getAllValues().get(1).getStatus());
    }
}
