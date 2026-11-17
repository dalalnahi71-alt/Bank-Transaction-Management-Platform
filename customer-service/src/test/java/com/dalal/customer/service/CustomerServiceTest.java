package com.dalal.customer.service;

import com.dalal.customer.entity.Customer;
import com.dalal.customer.exception.DuplicateResourceException;
import com.dalal.customer.exception.ResourceNotFoundException;
import com.dalal.customer.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    @Mock CustomerRepository repository;
    @InjectMocks CustomerService service;

    @Test
    void createCustomerSavesValidCustomer() {
        Customer customer = new Customer("Sara", "Amrani", "AB123456", "sara@example.com", 22);
        when(repository.existsByCinIgnoreCase(customer.getCin())).thenReturn(false);
        when(repository.existsByEmailIgnoreCase(customer.getEmail())).thenReturn(false);
        when(repository.save(customer)).thenReturn(customer);

        assertEquals(customer, service.createCustomer(customer));
        verify(repository).save(customer);
    }

    @Test
    void createCustomerRejectsDuplicateCin() {
        Customer customer = new Customer("Sara", "Amrani", "AB123456", "sara@example.com", 22);
        when(repository.existsByCinIgnoreCase(customer.getCin())).thenReturn(true);
        assertThrows(DuplicateResourceException.class, () -> service.createCustomer(customer));
        verify(repository, never()).save(any());
    }

    @Test
    void getCustomerRejectsUnknownId() {
        when(repository.findById(99)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getCustomerById(99));
    }
}
