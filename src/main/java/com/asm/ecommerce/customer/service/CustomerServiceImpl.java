package com.asm.ecommerce.customer.service;

import com.asm.ecommerce.auth.dto.UserDto;
import com.asm.ecommerce.customer.client.UserClient;
import com.asm.ecommerce.customer.domain.CustomerModel;
import com.asm.ecommerce.customer.dto.response.DisplayResponse;
import com.asm.ecommerce.customer.mapper.response.DisplayMapper;
import com.asm.ecommerce.customer.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository repo;
    private final UserClient userClient; // hoặc UserRepository nếu cùng DB
    private final DisplayMapper displayMapper;

    //Display all
    @Override
    @Transactional(readOnly = true)
    public List<DisplayResponse> displayAll() {
        List<CustomerModel> customers = repo.findAll();
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
        List<CustomerModel> customers = repo.findByIsActiveTrue();
        List<UUID> ids = customers.stream().map(CustomerModel::getId).toList();
        Map<UUID, UserDto> usersByCustomerId = userClient.findByCustomerIds(ids);
        return customers.stream()
                .map(c -> displayMapper.display(c, usersByCustomerId.get(c.getId())))
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerModel> findById(UUID id){
        return repo.findById(id);
    }

    @Override
    @Transactional
    public CustomerModel create(CustomerModel input){
        return repo.save(input);
    }

    @Override
    @Transactional
    public CustomerModel update(UUID id, CustomerModel input) {
        CustomerModel current = repo.findById(id)
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
}
