package com.dalal.account.controller;

import com.dalal.account.dto.CreateAccountRequest;
import com.dalal.account.dto.MoneyRequest;
import com.dalal.account.dto.TransferRequest;
import com.dalal.account.entity.Account;
import com.dalal.account.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;
    public AccountController(AccountService accountService) { this.accountService = accountService; }

    @PostMapping
    public ResponseEntity<Account> create(@Valid @RequestBody CreateAccountRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(request));
    }
    @GetMapping
    public List<Account> getAll() { return accountService.getAllAccounts(); }
    @GetMapping("/{id}")
    public Account getById(@PathVariable Long id) { return accountService.getAccount(id); }
    @GetMapping("/reference/{reference}")
    public Account getByReference(@PathVariable String reference) { return accountService.getByReference(reference); }

    // These endpoints are used by Transaction Service in this learning project.
    @PostMapping("/{id}/deposit")
    public Account deposit(@PathVariable Long id, @Valid @RequestBody MoneyRequest request) {
        return accountService.deposit(id, request.amount());
    }
    @PostMapping("/{id}/withdraw")
    public Account withdraw(@PathVariable Long id, @Valid @RequestBody MoneyRequest request) {
        return accountService.withdraw(id, request.amount());
    }
    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@Valid @RequestBody TransferRequest request) {
        accountService.transfer(request.sourceAccountId(), request.destinationAccountId(), request.amount());
        return ResponseEntity.noContent().build();
    }
}
