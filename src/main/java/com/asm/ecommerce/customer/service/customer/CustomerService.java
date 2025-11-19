package com.asm.ecommerce.customer.service.customer;

import com.asm.ecommerce.customer.domain.Customer;
import com.asm.ecommerce.customer.dto.request.customer.UpdateAdminCustomerRequest;
import com.asm.ecommerce.customer.dto.response.customer.DisplayAdminCustomerResponse;
import com.asm.ecommerce.customer.dto.CustomerDTO;
import com.asm.ecommerce.customer.dto.request.customer.CreateCustomerRequest;
import com.asm.ecommerce.shared.dto.ApiResponse;
import com.asm.ecommerce.shared.dto.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

    //Display

    //Display all
    ApiResponse<PageResponse<DisplayAdminCustomerResponse>> displayAll(Pageable pageable);

    //Display active customer
    ApiResponse<PageResponse<DisplayAdminCustomerResponse>> displayActive(Pageable pageable);


    //Find by Id
//    Optional<Customer> findById(UUID id);

    @Transactional(readOnly = true)
    ApiResponse<DisplayAdminCustomerResponse> displayByUserId(UUID userId);

    //Find by Phone
    @Transactional(readOnly = true)
    Optional<Customer> findByPhone(String phone);

    //Find by Email
    @Transactional(readOnly = true)
    Optional<Customer> findByFullname(String fullname);

    //CRUD

    //Create
    Customer create(Customer input);

    //Update
    ApiResponse<DisplayAdminCustomerResponse> update(UUID id, UpdateAdminCustomerRequest input);

    //Soft delete
    void softDelete(UUID id);

    //Create <- register
    CustomerDTO createCustomer(CreateCustomerRequest request);
}
