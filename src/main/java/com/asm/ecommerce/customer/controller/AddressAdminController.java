package com.asm.ecommerce.customer.controller;

import com.asm.ecommerce.customer.dto.request.address.UpdateAdminAddressRequest;
import com.asm.ecommerce.customer.dto.response.address.DisplayAdminAddressResponse;
import com.asm.ecommerce.customer.service.address.AddressService;
import com.asm.ecommerce.shared.dto.ApiResponse;
import com.asm.ecommerce.shared.dto.PageResponse;
import com.asm.ecommerce.shared.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PageResponse<DisplayAdminAddressResponse>>> listAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        ApiResponse<PageResponse<DisplayAdminAddressResponse>> response = service.displayAll(pageable);

        return ResponseEntity.ok(response);
    }

    // GET: /api/addresses/active
    // METHOD: Display Active
    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PageResponse<DisplayAdminAddressResponse>>> listActive(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        ApiResponse<PageResponse<DisplayAdminAddressResponse>> response = service.displayActive(pageable);

        return ResponseEntity.ok(response);
    }

    //Thieu displayByUserId
    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<?> listActiveByUserId(@AuthenticationPrincipal UserPrincipal userPrincipal){
        if(userPrincipal == null){
            return new ResponseEntity<>("Người dùng chưa được xác thực hoặc không tìm thấy thông tin.", HttpStatus.UNAUTHORIZED);
        }

        UUID id = userPrincipal.getId();

        ApiResponse<List<DisplayAdminAddressResponse>> response = service.displayByUserId(id);

        return ResponseEntity.ok(response);
    }

    //POST: /api/addresses/{id}
    // METHOD: create new addresses
    // Request -> userId
    @PostMapping()
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<?> create(@AuthenticationPrincipal UserPrincipal userPrincipal, @Valid @RequestBody UpdateAdminAddressRequest input){

        if (userPrincipal == null) {
            return new ResponseEntity<>("Người dùng chưa được xác thực hoặc không tìm thấy thông tin.", HttpStatus.UNAUTHORIZED);
        }

        UUID id = userPrincipal.getId();

        ApiResponse<DisplayAdminAddressResponse> response = service.create(id, input);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // PUT: /api/addresses/{id}
    // METHOD: update new customer
    //Request -> addressesId
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<ApiResponse<DisplayAdminAddressResponse>> update(@PathVariable UUID id, @Valid @RequestBody UpdateAdminAddressRequest input){
        ApiResponse<DisplayAdminAddressResponse> response = service.update(id, input);
        return ResponseEntity.ok(response);
    }

    // DELETE: /api/addresses/{id}
    // Request -> addressesId
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<Void> softDelete(@PathVariable UUID id){
        service.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    //GET: /api/customers/{id}
    // METHOD: Display profile
    // ---> Profile
    // Request -> userId

    //Hải làm ADMIN ADDRESS
// Admin: List addresses of a customer
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> adminListByCustomer(@PathVariable UUID customerId){
        return ResponseEntity.ok(service.adminListByCustomerId(customerId));
    }

    // Admin: Create address for a customer
    @PostMapping("/customer/{customerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> adminCreate(@PathVariable UUID customerId,
                                         @Valid @RequestBody UpdateAdminAddressRequest input){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.adminCreateByCustomerId(customerId, input));
    }

    // Admin: Set default address
    @PatchMapping("/{addressId}/set-default")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> adminSetDefault(@PathVariable UUID addressId){
        return ResponseEntity.ok(service.adminSetDefault(addressId));
    }


}
