package com.asm.ecommerce.customer.controller;

import com.asm.ecommerce.customer.domain.CustomerModel;
import com.asm.ecommerce.customer.dto.response.DisplayResponse;
import com.asm.ecommerce.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    // GET: /api/customers
    // METHOD: Display all
    @GetMapping
    public List<DisplayResponse> listAll(){
        return service.displayAll();
    }

    // GET: /api/customers/active
    // METHOD: Display active customers
    @GetMapping("/active")
    public List<DisplayResponse> listActive(){
        return service.displayActive();
    }


    @GetMapping("/{id}")
    public ResponseEntity<CustomerModel> findById(@PathVariable UUID id){
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    // POST: /api/customers
    // METHOD: create new customer
    @PostMapping
    public ResponseEntity<CustomerModel> create(@Valid @RequestBody CustomerModel input) {
        CustomerModel created = service.create(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT: /api/customers/{id}
    // METHOD: update customer
    @PutMapping("/{id}")
    public ResponseEntity<CustomerModel> update(@PathVariable UUID id,
                                                @Valid @RequestBody CustomerModel input) {
        CustomerModel updated = service.update(id, input);
        return ResponseEntity.ok(updated);
    }


    // DELETE (soft): /api/customers/{id}
    // METHOD: Soft delete customer
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDelete(@PathVariable UUID id) {
        service.softDelete(id);
        return ResponseEntity.noContent().build();
    }
}
