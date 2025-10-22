package com.asm.ecommerce.customer.service.address;

import com.asm.ecommerce.customer.domain.Address;
import com.asm.ecommerce.customer.dto.request.address.UpdateAdminAddressRequest;
import com.asm.ecommerce.customer.dto.response.address.DisplayAdminAddressResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AddressService {

    //Display all
    List<DisplayAdminAddressResponse> displayAll();

    //Display active address
    List<DisplayAdminAddressResponse> displayActive();

    //Find by CustomerId
//    Optional<Address> findByCustomerId(UUID id);

    //CRUD
    //Create
    @Transactional
    void create(UUID id, UpdateAdminAddressRequest input);

    //Update
    void update(UUID id, UpdateAdminAddressRequest input);

    //Soft delete
    void softDelete(UUID id);
}
