package com.asm.ecommerce.customer.controller;

import com.asm.ecommerce.customer.dto.request.address.UpdateAdminAddressRequest;
import com.asm.ecommerce.customer.dto.response.address.DisplayAdminAddressResponse;
import com.asm.ecommerce.customer.service.address.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressAdminController {

    private final AddressService service;

    // GET: /api/addresses
    // METHOD: Display All
    @GetMapping
    public List<DisplayAdminAddressResponse> listAll(){
        return service.displayAll();
    }

    // GET: /api/addresses/active
    // METHOD: Display Active
    @GetMapping("/active")
    public List<DisplayAdminAddressResponse> listActive(){
        return service.displayActive();
    }

    //POST: /api/addresses/{id}
    // METHOD: create new addresses
    @PostMapping("/{id}")
    public ResponseEntity<Void> create(@PathVariable UUID id, @Valid @RequestBody UpdateAdminAddressRequest input){
        service.create(id, input);
        return ResponseEntity.noContent().build();
    }

    // PUT: /api/addresses/{id}
    // METHOD: update new customer
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable UUID id, @Valid @RequestBody UpdateAdminAddressRequest input){
        service.update(id, input);
        return ResponseEntity.noContent().build();
    }

    // DELETE: /api/addresses/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDelete(@PathVariable UUID id){
        service.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    //GET: /api/customers/{id}
    // METHOD: Display profile
    // ---> Profile
    // Request -> userId
}
