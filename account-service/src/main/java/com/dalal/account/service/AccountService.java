package com.dalal.account.service;

import com.dalal.account.dto.CreateAccountRequest;
import com.dalal.account.entity.Account;
import com.dalal.account.entity.AccountStatus;
import com.dalal.account.exception.BusinessRuleException;
import com.dalal.account.exception.ResourceNotFoundException;
import com.dalal.account.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) { this.accountRepository = accountRepository; }

    @Transactional
    public Account createAccount(CreateAccountRequest request) {
        Account account = new Account(generateReference(), request.customerId(), request.accountType(), request.initialBalance());
        return accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public Account getAccount(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Account getByReference(String reference) {
        return accountRepository.findByAccountReference(reference)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with reference: " + reference));
    }

    @Transactional(readOnly = true)
    public List<Account> getAllAccounts() { return accountRepository.findAll(); }

    @Transactional
    public Account deposit(Long id, BigDecimal amount) {
        validatePositiveAmount(amount);
        Account account = getAccount(id);
        ensureActive(account);
        account.credit(amount);
        return accountRepository.save(account);
    }

    @Transactional
    public Account withdraw(Long id, BigDecimal amount) {
        validatePositiveAmount(amount);
        Account account = getAccount(id);
        ensureActive(account);
        if (account.getBalance().compareTo(amount) < 0) {
            throw new BusinessRuleException("Insufficient balance for this withdrawal");
        }
        account.debit(amount);
        return accountRepository.save(account);
    }

    @Transactional
    public void transfer(Long sourceId, Long destinationId, BigDecimal amount) {
        validatePositiveAmount(amount);
        if (sourceId.equals(destinationId)) {
            throw new BusinessRuleException("Source and destination accounts must be different");
        }
        Account source = getAccount(sourceId);
        Account destination = getAccount(destinationId);
        ensureActive(source);
        ensureActive(destination);
        if (source.getBalance().compareTo(amount) < 0) {
            throw new BusinessRuleException("Insufficient balance for this transfer");
        }
        source.debit(amount);
        destination.credit(amount);
        accountRepository.save(source);
        accountRepository.save(destination);
    }

    private void validatePositiveAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException("Amount must be greater than zero");
        }
    }

    private void ensureActive(Account account) {
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new BusinessRuleException("Account is not active");
        }
    }

    private String generateReference() { return "ACC-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase(); }
}
