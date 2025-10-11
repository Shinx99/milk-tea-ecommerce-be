package com.asm.ecommerce.customer.service;

import com.asm.ecommerce.customer.domain.CustomerModel;
import com.asm.ecommerce.customer.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repo;

    @Override
    @Transactional(readOnly = true)
    public List<CustomerModel> listAll(){
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerModel> listActive(){
        return repo.findByIsActiveTrue();
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
        if(input.getEmail() != null) current.setEmail((input.getEmail()));
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
