package com.dalal.customer.service;

import com.dalal.customer.entity.Customer;
import com.dalal.customer.exception.BadRequestException;
import com.dalal.customer.exception.DuplicateResourceException;
import com.dalal.customer.exception.ResourceNotFoundException;
import com.dalal.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public Customer createCustomer(Customer customer) {
        validateAdult(customer.getAge());
        if (customerRepository.existsByCinIgnoreCase(customer.getCin())) {
            throw new DuplicateResourceException("A customer with this CIN already exists");
        }
        if (customerRepository.existsByEmailIgnoreCase(customer.getEmail())) {
            throw new DuplicateResourceException("A customer with this email already exists");
        }
        return customerRepository.save(customer);
    }

    @Transactional(readOnly = true)
    public Customer getCustomerById(Integer id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Transactional
    public Customer updateCustomer(Integer id, Customer updatedCustomer) {
        validateAdult(updatedCustomer.getAge());
        Customer existing = getCustomerById(id);

        if (customerRepository.existsByCinIgnoreCaseAndIdNot(updatedCustomer.getCin(), id)) {
            throw new DuplicateResourceException("Another customer already uses this CIN");
        }
        if (customerRepository.existsByEmailIgnoreCaseAndIdNot(updatedCustomer.getEmail(), id)) {
            throw new DuplicateResourceException("Another customer already uses this email");
        }

        existing.setFirstName(updatedCustomer.getFirstName());
        existing.setLastName(updatedCustomer.getLastName());
        existing.setCin(updatedCustomer.getCin());
        existing.setEmail(updatedCustomer.getEmail());
        existing.setAge(updatedCustomer.getAge());
        return customerRepository.save(existing);
    }

    @Transactional
    public void deleteCustomer(Integer id) {
        Customer customer = getCustomerById(id);
        customerRepository.delete(customer);
    }

    private void validateAdult(Integer age) {
        if (age == null || age < 18) {
            throw new BadRequestException("Customer must be at least 18 years old");
        }
    }
}
