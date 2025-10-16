package com.asm.ecommerce.customer.service;

import com.asm.ecommerce.auth.dto.UserDto;
import com.asm.ecommerce.customer.client.UserClient;
import com.asm.ecommerce.customer.domain.Customer;
import com.asm.ecommerce.customer.dto.response.DisplayResponse;
import com.asm.ecommerce.customer.mapper.CustomerMapper;
import com.asm.ecommerce.customer.mapper.response.DisplayMapper;
import com.asm.ecommerce.customer.dto.CustomerDTO;
import com.asm.ecommerce.customer.dto.request.CreateCustomerRequest;
import com.asm.ecommerce.customer.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository repo;
    private final UserClient userClient; // hoặc UserRepository nếu cùng DB
    private final DisplayMapper displayMapper;
    private final CustomerMapper customerMapper;

    //Display all
    @Override
    @Transactional(readOnly = true)
    public List<DisplayResponse> displayAll() {
        List<Customer> customers = repo.findAll();
        List<UUID> ids = customers.stream().filter(Objects::nonNull).map(c -> c.getId()).toList();
        Map<UUID, UserDto> usersByCustomerId = userClient.findByCustomerIds(ids); // batch
        return customers.stream()
                .map(c -> displayMapper.display(c, usersByCustomerId.get(c.getId())))
                .toList();
    }

    //Display with
    @Override
    @Transactional(readOnly = true)
    public List<DisplayResponse> displayActive() {
        List<Customer> customers = repo.findByIsActiveTrue();
        List<UUID> ids = customers.stream().map(Customer::getId).toList();
        Map<UUID, UserDto> usersByCustomerId = userClient.findByCustomerIds(ids);
        return customers.stream()
                .map(c -> displayMapper.display(c, usersByCustomerId.get(c.getId())))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Customer> findById(UUID id){
        return repo.findById(id);
    }

    @Override
    @Transactional
    public Customer create(Customer input){
        return repo.save(input);
    }

    @Override
    @Transactional
    public Customer update(UUID id, Customer input) {
        Customer current = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        if(input.getPhone() != null) current.setPhone(input.getPhone());
        if(input.getFullname() != null) current.setFullname(input.getFullname());
        return current;
    }

    @Override
    @Transactional
    public void softDelete(UUID id){
        int updated = repo.softDeleteById(id, Instant.now());
        if(updated == 0){
            throw new EntityNotFoundException("Customer not found or already inactive");
        }
    }


    //Register

    // customer/service/CustomerServiceImpl.java

    @Override
    @Transactional
    public CustomerDTO createCustomer(CreateCustomerRequest request) {
        log.info("Creating customer for userId: {}", request.getUserId());


        // Create Customer entity
        Customer customer = customerMapper.toEntity(request.getUserId(),request.getFullName(),request.getPhone());

        Customer saved = repo.save(customer);

        // Map to DTO
        return CustomerDTO.builder()
                .id(saved.getId())
                .userId(saved.getUserId())
                .phone(saved.getPhone())
                .fullname(saved.getFullname())
                .isActive(saved.isActive())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                // email sẽ được add sau nếu cần (từ User entity)
                .build();
    }


}
