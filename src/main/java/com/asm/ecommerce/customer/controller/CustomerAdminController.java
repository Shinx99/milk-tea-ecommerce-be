package com.asm.ecommerce.customer.controller;

import com.asm.ecommerce.customer.dto.request.customer.UpdateAdminCustomerRequest;
import com.asm.ecommerce.customer.dto.response.customer.DisplayAdminCustomerResponse;
import com.asm.ecommerce.customer.service.customer.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerAdminController {

    private final CustomerService service;

    // GET: /api/customers
    // METHOD: Display all
    @GetMapping
    public List<DisplayAdminCustomerResponse> listAll(){
        return service.displayAll();
    }

    // GET: /api/customers/active
    // METHOD: Display active customers
    @GetMapping("/active")
    public List<DisplayAdminCustomerResponse> listActive(){
        return service.displayActive();
    }


//    @GetMapping("/{id}")
//    public ResponseEntity<Customer> findById(@PathVariable UUID id){
//        return service.findById(id)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }



    // POST: /api/customers
    // METHOD: create new customer
//    @PostMapping
//    public ResponseEntity<Customer> create(@Valid @RequestBody Customer input) {
//        Customer created = service.create(input);
//        return ResponseEntity.status(HttpStatus.CREATED).body(created);
//    }

    // PUT: /api/customers/{id}
    // METHOD: update customer
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable UUID id, @Valid @RequestBody UpdateAdminCustomerRequest input) {
        service.update(id, input);
        return ResponseEntity.noContent().build();
    }


    // DELETE (soft): /api/customers/{id}
    // METHOD: Soft delete customer
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDelete(@PathVariable UUID id) {
        service.softDelete(id);
        return ResponseEntity.noContent().build();
    }
}
