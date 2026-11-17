package com.dalal.transaction.controller;

import com.dalal.transaction.dto.MoneyTransactionRequest;
import com.dalal.transaction.dto.TransferTransactionRequest;
import com.dalal.transaction.entity.BankTransaction;
import com.dalal.transaction.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    public TransactionController(TransactionService transactionService) { this.transactionService = transactionService; }

    @PostMapping("/deposit")
    public ResponseEntity<BankTransaction> deposit(@Valid @RequestBody MoneyTransactionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.deposit(request));
    }
    @PostMapping("/withdraw")
    public ResponseEntity<BankTransaction> withdraw(@Valid @RequestBody MoneyTransactionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.withdraw(request));
    }
    @PostMapping("/transfer")
    public ResponseEntity<BankTransaction> transfer(@Valid @RequestBody TransferTransactionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.transfer(request));
    }
    @GetMapping
    public List<BankTransaction> getAll() { return transactionService.getAll(); }
    @GetMapping("/{id}")
    public BankTransaction getById(@PathVariable Long id) { return transactionService.getById(id); }
}
