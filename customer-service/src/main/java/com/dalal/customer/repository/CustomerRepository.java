package com.dalal.customer.repository;

import com.dalal.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    boolean existsByCinIgnoreCase(String cin);
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByCinIgnoreCaseAndIdNot(String cin, Integer id);
    boolean existsByEmailIgnoreCaseAndIdNot(String email, Integer id);
}
