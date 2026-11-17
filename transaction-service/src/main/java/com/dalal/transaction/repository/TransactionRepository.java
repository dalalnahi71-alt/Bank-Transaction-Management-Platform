package com.dalal.transaction.repository;
import com.dalal.transaction.entity.BankTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TransactionRepository extends JpaRepository<BankTransaction, Long> { }
