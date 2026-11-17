package com.dalal.transaction.service;

import com.dalal.transaction.client.AccountServiceClient;
import com.dalal.transaction.dto.*;
import com.dalal.transaction.entity.*;
import com.dalal.transaction.exception.AccountOperationException;
import com.dalal.transaction.exception.ResourceNotFoundException;
import com.dalal.transaction.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestClientException;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository repository;
    private final AccountServiceClient accountClient;

    public TransactionService(TransactionRepository repository, AccountServiceClient accountClient) {
        this.repository = repository;
        this.accountClient = accountClient;
    }

    public BankTransaction deposit(MoneyTransactionRequest request) {
        BankTransaction transaction = start(TransactionType.DEPOSIT, null, request.accountId(), request.amount(), request.description());
        try {
            accountClient.deposit(request.accountId(), new AccountMoneyRequest(request.amount()));
            return complete(transaction);
        } catch (RestClientException ex) {
            return fail(transaction, ex);
        }
    }

    public BankTransaction withdraw(MoneyTransactionRequest request) {
        BankTransaction transaction = start(TransactionType.WITHDRAWAL, request.accountId(), null, request.amount(), request.description());
        try {
            accountClient.withdraw(request.accountId(), new AccountMoneyRequest(request.amount()));
            return complete(transaction);
        } catch (RestClientException ex) {
            return fail(transaction, ex);
        }
    }

    public BankTransaction transfer(TransferTransactionRequest request) {
        if (request.sourceAccountId().equals(request.destinationAccountId())) {
            throw new AccountOperationException("Source and destination accounts must be different");
        }
        BankTransaction transaction = start(TransactionType.TRANSFER, request.sourceAccountId(), request.destinationAccountId(), request.amount(), request.description());
        try {
            accountClient.transfer(new AccountTransferRequest(request.sourceAccountId(), request.destinationAccountId(), request.amount()));
            return complete(transaction);
        } catch (RestClientException ex) {
            return fail(transaction, ex);
        }
    }

    @Transactional(readOnly = true)
    public List<BankTransaction> getAll() { return repository.findAll(); }

    @Transactional(readOnly = true)
    public BankTransaction getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));
    }

    private BankTransaction start(TransactionType type, Long source, Long destination,
                                  java.math.BigDecimal amount, String description) {
        return repository.save(new BankTransaction(type, source, destination, amount, description));
    }

    private BankTransaction complete(BankTransaction transaction) {
        transaction.markCompleted();
        return repository.save(transaction);
    }

    private BankTransaction fail(BankTransaction transaction, RestClientException exception) {
        transaction.markFailed();
        repository.save(transaction);
        String detail = exception instanceof RestClientResponseException responseException
                ? "Account Service returned HTTP " + responseException.getStatusCode().value()
                : "Account Service is unavailable";
        throw new AccountOperationException("Transaction failed: " + detail);
    }
}
