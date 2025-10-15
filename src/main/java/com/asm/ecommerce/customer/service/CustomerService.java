package com.asm.ecommerce.customer.service;

import com.asm.ecommerce.customer.domain.CustomerModel;
import com.asm.ecommerce.customer.dto.response.DisplayResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

    List<DisplayResponse> displayAll();

    List<DisplayResponse> displayActive();

    Optional<CustomerModel> findById(UUID id);

    CustomerModel create(CustomerModel input);

    CustomerModel update(UUID id, CustomerModel input);

    void softDelete(UUID id);
}
