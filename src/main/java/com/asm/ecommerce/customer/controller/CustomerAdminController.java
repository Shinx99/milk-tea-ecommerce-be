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

    //GET: /api/customers/{id}
    // METHOD: Display profile
    // ---> Profile
    // Request -> userId
    @GetMapping("/{id}")
    public ResponseEntity<DisplayAdminCustomerResponse> getCustomerById(@PathVariable UUID id) {

        // Gọi service đã viết trước đó để tìm thông tin
        DisplayAdminCustomerResponse customerProfile = service.displayByUserId(id);

        // Trả về dữ liệu
        return ResponseEntity.ok(customerProfile);
    }

    // PUT: /api/customers/{id}
    // METHOD: update customer
    // ---> Cap nhat dia chi (Address)
    // Request -> userId
    @PutMapping("/{id}")
    public ResponseEntity<DisplayAdminCustomerResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateAdminCustomerRequest input) {
        DisplayAdminCustomerResponse updatedCustomer = service.update(id, input);
        return ResponseEntity.ok(updatedCustomer);
    }

    // DELETE (soft): /api/customers/{id}
    // METHOD: Soft delete customer
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDelete(@PathVariable UUID id) {
        service.softDelete(id);
        return ResponseEntity.noContent().build();
    }
}
