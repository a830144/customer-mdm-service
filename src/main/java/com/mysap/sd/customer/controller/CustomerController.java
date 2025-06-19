package com.mysap.sd.customer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mysap.sd.customer.entity.Customer;
import com.mysap.sd.customer.repository.CustomerRepository;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/customers")
public class CustomerController {
	
	private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerRepository repository;

    public CustomerController(CustomerRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    @Operation(summary = "ADD A NEW CUSTOMER")
    public ResponseEntity<?> createCustomer(@RequestBody Customer customer) {
        if (repository.existsByEmail(customer.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists.");
        }
        if (repository.existsById(customer.getId())) {
            return ResponseEntity.badRequest().body("Tax ID already exists.");
        }
        Customer saved = repository.save(customer);
        log.info("customer created: {}", customer.getId());
        log.warn("Payment delay for {}", customer.getId());
        return ResponseEntity.status(201).body(saved);
    }
    
    @GetMapping
    @Operation(summary = "Get ALL customers")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }
}