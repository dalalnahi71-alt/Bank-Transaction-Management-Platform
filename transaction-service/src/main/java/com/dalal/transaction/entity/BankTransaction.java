package com.dalal.transaction.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "bank_transactions", uniqueConstraints = @UniqueConstraint(name = "uk_transaction_reference", columnNames = "transaction_reference"))
public class BankTransaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "transaction_reference", nullable = false, unique = true, length = 50)
    private String transactionReference;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20)
    private TransactionType type;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20)
    private TransactionStatus status;
    private Long sourceAccountId;
    private Long destinationAccountId;
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;
    private String description;
    @Column(nullable = false)
    private Instant createdAt;

    public BankTransaction() { }
    public BankTransaction(TransactionType type, Long sourceAccountId, Long destinationAccountId, BigDecimal amount, String description) {
        this.transactionReference = "TXN-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();
        this.type = type;
        this.status = TransactionStatus.PENDING;
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.amount = amount;
        this.description = description;
        this.createdAt = Instant.now();
    }
    public void markCompleted() { this.status = TransactionStatus.COMPLETED; }
    public void markFailed() { this.status = TransactionStatus.FAILED; }
    public Long getId() { return id; }
    public String getTransactionReference() { return transactionReference; }
    public TransactionType getType() { return type; }
    public TransactionStatus getStatus() { return status; }
    public Long getSourceAccountId() { return sourceAccountId; }
    public Long getDestinationAccountId() { return destinationAccountId; }
    public BigDecimal getAmount() { return amount; }
    public String getDescription() { return description; }
    public Instant getCreatedAt() { return createdAt; }
}
