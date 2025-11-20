package com.asm.ecommerce.customer.service.address;

import com.asm.ecommerce.customer.domain.Address;
import com.asm.ecommerce.customer.domain.Customer;
import com.asm.ecommerce.customer.dto.request.address.UpdateAdminAddressRequest;
import com.asm.ecommerce.customer.dto.response.address.DisplayAdminAddressResponse;
import com.asm.ecommerce.customer.mapper.request.address.UpdateAdminAddressMapper;
import com.asm.ecommerce.customer.mapper.response.address.DisplayAddressMapper;
import com.asm.ecommerce.customer.repository.AddressRepository;
import com.asm.ecommerce.customer.repository.CustomerRepository;
import com.asm.ecommerce.shared.dto.ApiResponse;
import com.asm.ecommerce.shared.dto.PageResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@Slf4j
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
    public ApiResponse<PageResponse<DisplayAdminAddressResponse>> displayAll(Pageable pageable) {
        Page<Address> addressPage = repo.findAllWithCustomer(pageable);
        PageResponse<DisplayAdminAddressResponse> pageResponse = PageResponse.<DisplayAdminAddressResponse>builder()
                .content(addressPage.getContent().stream()
                        .map(address -> responseMapper.display(address.getCustomer(), address))
                        .toList())
                .pageNumber(addressPage.getNumber())
                .pageSize(addressPage.getSize())
                .totalPages(addressPage.getTotalPages())
                .totalElements(addressPage.getTotalElements())
                .last(addressPage.isLast())
                .build();

        return ApiResponse.<PageResponse<DisplayAdminAddressResponse>>builder()
                .success(true)
                .message("Addresses retrieved successfully")
                .data(pageResponse)
                .build();
    }


    //display Active
    @Override
    @Transactional(readOnly = true)
    public ApiResponse<PageResponse<DisplayAdminAddressResponse>> displayActive(Pageable pageable){
        Page<Address> addresses = repo.findAllWithCustomerByActiveTrue(pageable);
        PageResponse<DisplayAdminAddressResponse> pageResponse = PageResponse.<DisplayAdminAddressResponse>builder()
            .content(addresses.stream()
                    .map(address -> responseMapper.display(address.getCustomer(), address))
                    .toList())
                .pageNumber(addresses.getNumber())
                .pageSize(addresses.getSize())
                .totalPages(addresses.getTotalPages())
                .totalElements(addresses.getTotalElements())
                .last(addresses.isLast())
                .build();

        return ApiResponse.<PageResponse<DisplayAdminAddressResponse>> builder()
                .success(true)
                .message("Active Addresses retrieved successfully")
                .data(pageResponse)
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse<List<DisplayAdminAddressResponse>> displayByUserId(UUID userId){
        Customer customer = customerRepo.findByUserIdAndActiveTrue(userId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy khách hàng với userId: " + userId));

        UUID customerId = customer.getId();

        List<Address> addressList = repo.findAllWithCustomerByActiveTrue(customerId);

        List<DisplayAdminAddressResponse> response = addressList.stream()
                .map(address -> responseMapper.display(address.getCustomer(), address)).toList();


        return ApiResponse.<List<DisplayAdminAddressResponse>> builder()
                .success(true)
                .message("Get Addresses List successfully")
                .data(response)
                .build();
    }

    // Su dung customerID truyen xuong de xu ly
    @Transactional
    @Override
    public ApiResponse<DisplayAdminAddressResponse> create(UUID userId, UpdateAdminAddressRequest input) {

        Customer customer = customerRepo.findByUserIdAndActiveTrue(userId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy khách hàng với userId: " + userId));

        UUID customerId = customer.getId();

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

        DisplayAdminAddressResponse response =  responseMapper.display(customer, newAddress);

        return ApiResponse.<DisplayAdminAddressResponse>builder()
                .success(true)
                .message("Address created successfully")
                .data(response)
                .build();
    }


    //Su dung id cua chinh bang Address de xu ly
    @Transactional
    @Override
    public ApiResponse<DisplayAdminAddressResponse> update(UUID addressId, UpdateAdminAddressRequest input) {
        Address current = repo.findById(addressId)
                .orElseThrow(() -> new EntityNotFoundException("AddressId not found"));

        UUID customerId = repo.findCustomerIdByAddressId(addressId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found for this address"));

        if (Boolean.TRUE.equals(input.getIsDefault())) {

            repo.findDefaultAddressIdByCustomerId(customerId).ifPresent(oldId -> {
                if (!oldId.equals(addressId)) {
                    repo.clearDefaultAddressForCustomer(oldId);
                    repo.flush(); // đảm bảo UPDATE trước khi set default cho current
                }
            });
        }

        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("CustomerId not found"));

        requestMapper.updateAdminAddress(current, input);
        repo.save(current);

        DisplayAdminAddressResponse response = responseMapper.display(customer, current);

        return ApiResponse.<DisplayAdminAddressResponse>builder()
                .success(true)
                .message("Addresses updated successfully")
                .data(response)
                .build();
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
