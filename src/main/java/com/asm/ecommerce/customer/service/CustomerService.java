package com.asm.ecommerce.customer.service;

import com.asm.ecommerce.customer.domain.CustomerModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

    List<CustomerModel> listAll();

    List<CustomerModel> listActive();

    Optional<CustomerModel> findById(UUID id);

    CustomerModel create(CustomerModel input);

    CustomerModel update(UUID id, CustomerModel input);

    void softDelete(UUID id);
}
