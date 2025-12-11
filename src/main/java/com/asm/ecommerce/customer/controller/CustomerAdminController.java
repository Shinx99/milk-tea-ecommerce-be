package com.asm.ecommerce.customer.controller;

import com.asm.ecommerce.auth.domain.User;
import com.asm.ecommerce.customer.dto.request.customer.UpdateAdminCustomerRequest;
import com.asm.ecommerce.customer.dto.response.customer.DisplayAdminCustomerResponse;
import com.asm.ecommerce.customer.service.customer.CustomerService;
import com.asm.ecommerce.shared.dto.ApiResponse;
import com.asm.ecommerce.shared.dto.PageResponse;
import com.asm.ecommerce.shared.security.UserPrincipal;
import com.asm.ecommerce.shared.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.UUID;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerAdminController {

    private final CustomerService service;

    // GET: /api/customers
    // METHOD: Display all
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PageResponse<DisplayAdminCustomerResponse>>> listAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortby,
            @RequestParam(defaultValue = "DESC") String direction
            ){

        // THÊM ĐOẠN DEBUG VÀO ĐÂY------------------------------------------------------------------
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            System.out.println("Authenticated User: " + auth.getName());
            System.out.println("User's Authorities: " + auth.getAuthorities());
        } else {
            System.out.println("No authentication found.");
        }
        //-------------------------------------------------------------------------------------------

        Sort.Direction sortDirection = direction.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortby));
        ApiResponse<PageResponse<DisplayAdminCustomerResponse>> response =  service.displayAll(keyword, pageable);
        return ResponseEntity.ok(response);
    }

    // GET: /api/customers/active
    // METHOD: Display active customers
    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PageResponse<DisplayAdminCustomerResponse>>> listActive(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction){

        // THÊM ĐOẠN DEBUG VÀO ĐÂY------------------------------------------------------------------
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            System.out.println("Authenticated User: " + auth.getPrincipal());
            System.out.println("User's Authorities: " + auth.getAuthorities());
        } else {
            System.out.println("No authentication found.");
        }
        //-------------------------------------------------------------------------------------------

        Sort.Direction sortDirection = direction.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        ApiResponse<PageResponse<DisplayAdminCustomerResponse>> response = service.displayActive(pageable);

        return ResponseEntity.ok(response);
    }

    //GET: /api/customers/{id}
    // METHOD: Display profile
    // ---> Profile
    // Request -> userId
    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<?> getCustomerById(@AuthenticationPrincipal UserPrincipal userPrincipal) {

        if (userPrincipal == null) {
            return new ResponseEntity<>("Người dùng chưa được xác thực hoặc không tìm thấy thông tin.", HttpStatus.UNAUTHORIZED);
        }

        UUID userId = userPrincipal.getId();

        // BƯỚC 3: Gọi service như bình thường
        ApiResponse<DisplayAdminCustomerResponse> response = service.displayByUserId(userId);
        return ResponseEntity.ok(response);
    }

    // PUT: /api/customers
    // METHOD: update customer
    // ---> Cap nhat dia chi (Address)
    // Request -> userId
    @PutMapping()
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<?> update(@AuthenticationPrincipal UserPrincipal userPrincipal, @Valid @RequestBody UpdateAdminCustomerRequest input) {

        if (userPrincipal == null) {
            return new ResponseEntity<>("Người dùng chưa được xác thực hoặc không tìm thấy thông tin.", HttpStatus.UNAUTHORIZED);
        }

        UUID id = userPrincipal.getId();
        ApiResponse<DisplayAdminCustomerResponse> updatedCustomer = service.update(id, input);
        return ResponseEntity.ok(updatedCustomer);
    }

    // PUT: /api/customers/admin
    // METHOD: update customer for Admin page
    // Request -> customerId
    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateForAdmin(@PathVariable UUID id, @Valid @RequestBody UpdateAdminCustomerRequest input){
        if(id == null){
            return new ResponseEntity<>("Người dùng chưa được xác thực hoặc không tìm thấy thông tin.", HttpStatus.UNAUTHORIZED);
        }

        ApiResponse<DisplayAdminCustomerResponse> updatedCustomer = service.updateAdmin(id, input);
        return ResponseEntity.ok(updatedCustomer);
    }


    // PUT: /api/customers
    // METHOD: soft delete customer for Admin page
    // Request -> customerId
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> softDelete(@PathVariable UUID id) {
        if(id == null){
            return new ResponseEntity<>("Người dùng chưa được xác thực hoặc không tìm thấy thông tin.", HttpStatus.UNAUTHORIZED);
        }

        service.softDelete(id);
        return ResponseEntity.noContent().build();
    }
}
