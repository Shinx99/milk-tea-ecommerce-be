package com.asm.ecommerce.customer.service.address;

import com.asm.ecommerce.customer.dto.request.address.UpdateAdminAddressRequest;
import com.asm.ecommerce.customer.dto.response.address.DisplayAdminAddressResponse;
import com.asm.ecommerce.shared.dto.ApiResponse;
import com.asm.ecommerce.shared.dto.PageResponse;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface AddressService {

    //Display all
    ApiResponse<PageResponse<DisplayAdminAddressResponse>> displayAll(Pageable pageable);

    //Display active address
    ApiResponse<PageResponse<DisplayAdminAddressResponse>> displayActive(Pageable pageable);

    //Find by CustomerId
//    Optional<Address> findByCustomerId(UUID id);

    @Transactional(readOnly = true)
    ApiResponse<List<DisplayAdminAddressResponse>> displayByUserId(UUID userId);

    //CRUD
    //Create
    @Transactional
    ApiResponse<DisplayAdminAddressResponse> create(UUID id, UpdateAdminAddressRequest input);

    //Update
    ApiResponse<DisplayAdminAddressResponse> update(UUID id, UpdateAdminAddressRequest input);

    //Soft delete
    void softDelete(UUID id);

    //Hải làm cho admin
    // Admin: List all addresses of a specific customer
    @Transactional(readOnly = true)
    ApiResponse<List<DisplayAdminAddressResponse>> adminListByCustomerId(UUID customerId);

    // Admin: Create address for a specific customer
    @Transactional
    ApiResponse<DisplayAdminAddressResponse> adminCreateByCustomerId(UUID customerId, UpdateAdminAddressRequest input);

    // Admin: Set default address for a customer
    @Transactional
    ApiResponse<DisplayAdminAddressResponse> adminSetDefault(UUID addressId);

    @Transactional(readOnly = true)
    DisplayAdminAddressResponse getOrderAddress(UUID customerId);

    @Transactional
    ApiResponse<DisplayAdminAddressResponse> adminUpdateAddress(UUID id, UpdateAdminAddressRequest input);


    @Transactional
    void adminDeactivateAddress(UUID id);
}
