package com.asm.ecommerce.customer.service.customer;

import com.asm.ecommerce.customer.domain.Customer;
import com.asm.ecommerce.customer.dto.request.customer.UpdateAdminCustomerRequest;
import com.asm.ecommerce.customer.dto.response.customer.DisplayAdminCustomerResponse;
import com.asm.ecommerce.customer.dto.CustomerDTO;
import com.asm.ecommerce.customer.dto.request.customer.CreateCustomerRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

    //Display

    //Display all
    List<DisplayAdminCustomerResponse> displayAll();

    //Display active customer
    List<DisplayAdminCustomerResponse> displayActive();

    //Find

    //Find by Id
//    Optional<Customer> findById(UUID id);

    @Transactional(readOnly = true)
    DisplayAdminCustomerResponse displayByUserId(UUID userId);

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
    DisplayAdminCustomerResponse update(UUID id, UpdateAdminCustomerRequest input);

    //Soft delete
    void softDelete(UUID id);

    //Create <- register
    CustomerDTO createCustomer(CreateCustomerRequest request);
}
