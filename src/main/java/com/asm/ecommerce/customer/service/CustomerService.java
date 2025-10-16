package com.asm.ecommerce.customer.service;

import com.asm.ecommerce.customer.domain.Customer;
import com.asm.ecommerce.customer.dto.response.DisplayResponse;
import com.asm.ecommerce.customer.dto.CustomerDTO;
import com.asm.ecommerce.customer.dto.request.CreateCustomerRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

    List<DisplayResponse> displayAll();

    List<DisplayResponse> displayActive();

    Optional<Customer> findById(UUID id);

    Customer create(Customer input);

    Customer update(UUID id, Customer input);

    void softDelete(UUID id);

    CustomerDTO createCustomer(CreateCustomerRequest request);
}
