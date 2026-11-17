package com.dalal.account.service;

import com.dalal.account.entity.*;
import com.dalal.account.exception.BusinessRuleException;
import com.dalal.account.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @Mock AccountRepository repository;
    @InjectMocks AccountService service;

    @Test
    void transferMovesMoneyBetweenAccounts() {
        Account source = new Account("ACC-ONE", 1, AccountType.CHECKING, new BigDecimal("500.00"));
        Account destination = new Account("ACC-TWO", 2, AccountType.SAVINGS, new BigDecimal("100.00"));
        when(repository.findById(1L)).thenReturn(Optional.of(source));
        when(repository.findById(2L)).thenReturn(Optional.of(destination));
        service.transfer(1L, 2L, new BigDecimal("75.00"));
        assertEquals(new BigDecimal("425.00"), source.getBalance());
        assertEquals(new BigDecimal("175.00"), destination.getBalance());
        verify(repository, times(2)).save(any(Account.class));
    }

    @Test
    void transferRejectsInsufficientBalance() {
        Account source = new Account("ACC-ONE", 1, AccountType.CHECKING, new BigDecimal("10.00"));
        Account destination = new Account("ACC-TWO", 2, AccountType.SAVINGS, new BigDecimal("100.00"));
        when(repository.findById(1L)).thenReturn(Optional.of(source));
        when(repository.findById(2L)).thenReturn(Optional.of(destination));
        assertThrows(BusinessRuleException.class, () -> service.transfer(1L, 2L, new BigDecimal("75.00")));
        assertEquals(new BigDecimal("10.00"), source.getBalance());
        verify(repository, never()).save(any());
    }
}
