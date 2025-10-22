package com.asm.ecommerce.customer.service.address;

import com.asm.ecommerce.customer.domain.Address;
import com.asm.ecommerce.customer.domain.Customer;
import com.asm.ecommerce.customer.dto.request.address.UpdateAdminAddressRequest;
import com.asm.ecommerce.customer.dto.response.address.DisplayAdminAddressResponse;
import com.asm.ecommerce.customer.mapper.request.address.UpdateAdminAddressMapper;
import com.asm.ecommerce.customer.mapper.response.address.DisplayAddressMapper;
import com.asm.ecommerce.customer.repository.AddressRepository;
import com.asm.ecommerce.customer.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final CustomerRepository customerRepo;
    private final AddressRepository repo;
    private final UpdateAdminAddressMapper requestMapper;
    private final DisplayAddressMapper responseMapper;


    //display All
    @Override
    @Transactional(readOnly = true)
    public List<DisplayAdminAddressResponse> displayAll(){
        List<Address> addresses = repo.findAllWithCustomer();
        return addresses.stream()
                .map(address -> responseMapper.display(address.getCustomer(), address))
                .collect(Collectors.toList());
    }

    //display Active
    @Override
    @Transactional(readOnly = true)
    public List<DisplayAdminAddressResponse> displayActive(){
        List<Address> addresses = repo.findAllWithCustomerByActiveTrue();
        return addresses.stream()
                .map(address -> responseMapper.display(address.getCustomer(), address))
                .collect(Collectors.toList());
    }

    // Su dung customerID truyen xuong de xu ly
    @Transactional
    @Override
    public void create(UUID customerId, UpdateAdminAddressRequest input) {
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("CustomerId not found"));

        if (Boolean.TRUE.equals(input.getIsDefault())) {
            repo.findDefaultAddressIdByCustomerId(customerId).ifPresent(oldId -> {
                repo.clearDefaultAddressForCustomer(oldId);
                repo.flush(); // đảm bảo UPDATE trước INSERT
            });
        }

        Address newAddress = new Address();
        requestMapper.updateAdminAddress(newAddress, input);
        newAddress.setCustomer(customer);
        repo.save(newAddress);
    }


    //Su dung id cua chinh bang Address de xu ly
    @Transactional
    @Override
    public void update(UUID addressId, UpdateAdminAddressRequest input) {
        Address current = repo.findById(addressId)
                .orElseThrow(() -> new EntityNotFoundException("AddressId not found"));

        if (Boolean.TRUE.equals(input.getIsDefault())) {
            UUID customerId = repo.findCustomerIdByAddressId(addressId)
                    .orElseThrow(() -> new EntityNotFoundException("Customer not found for this address"));

            repo.findDefaultAddressIdByCustomerId(customerId).ifPresent(oldId -> {
                if (!oldId.equals(addressId)) {
                    repo.clearDefaultAddressForCustomer(oldId);
                    repo.flush(); // đảm bảo UPDATE trước khi set default cho current
                }
            });
        }

        requestMapper.updateAdminAddress(current, input);
        repo.save(current);
    }


    //Su dung id cua bang address de xu ly
    @Override
    @Transactional
    public void softDelete(UUID id){
        Address current = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("AddressesID not found"));

        UUID customerId = repo.findCustomerIdByAddressId(id)
                    .orElseThrow(() -> new EntityNotFoundException("Customer not found for this address"));

        boolean wasDefault = Boolean.TRUE.equals(current.getIsDefault());

        int updated = repo.softDeleteById(id);
        if(updated == 0){
            throw new EntityNotFoundException("Address not found or already inactive");
        }

        if (wasDefault) {
            repo.findOneActiveAddressIdForCustomer(customerId, id).ifPresent(nextId -> {
                repo.setDefaultbyId(nextId);
                repo.flush();
            });
        }
    }


}
