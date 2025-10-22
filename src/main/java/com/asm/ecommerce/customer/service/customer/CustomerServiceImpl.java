package com.asm.ecommerce.customer.service.customer;

import com.asm.ecommerce.auth.dto.UserDto;
import com.asm.ecommerce.customer.client.UserClient;
import com.asm.ecommerce.customer.domain.Customer;
import com.asm.ecommerce.customer.dto.request.customer.UpdateAdminCustomerRequest;
import com.asm.ecommerce.customer.dto.response.customer.DisplayAdminCustomerResponse;
import com.asm.ecommerce.customer.mapper.CustomerMapper;
import com.asm.ecommerce.customer.mapper.request.customer.UpdateAdminCustomerMapper;
import com.asm.ecommerce.customer.mapper.response.customer.DisplayCustomerMapper;
import com.asm.ecommerce.customer.dto.CustomerDTO;
import com.asm.ecommerce.customer.dto.request.customer.CreateCustomerRequest;
import com.asm.ecommerce.customer.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository repo;
    private final UserClient userClient; // hoặc UserRepository nếu cùng DB
    private final UpdateAdminCustomerMapper updateAdminCustomerMapper;
    private final DisplayCustomerMapper displayCustomerMapper;
    private final CustomerMapper customerMapper;

    @Override
    @Transactional(readOnly = true)
    public List<DisplayAdminCustomerResponse> displayAll() {
        List<Customer> customers = repo.findAll();

        // 1. Lấy ra danh sách các userId từ list customer
        List<UUID> userIds = customers.stream()
                .map(Customer::getUserId) // Lấy ra trường UUID userId
                .filter(Objects::nonNull)
                .distinct() // Tránh gọi API với các ID trùng lặp
                .toList();

        // 2. Gọi sang user service để lấy thông tin user theo batch
        Map<UUID, UserDto> usersByUserId = userClient.findByUserIds(userIds);

        // 3. Map kết quả cuối cùng
        return customers.stream()
                .map(c -> {
                    // Tra cứu trong map bằng userId của customer
                    UserDto userDto = usersByUserId.get(c.getUserId());
                    return displayCustomerMapper.display(c, userDto);
                })
                .toList();
    }


    //Display active customer
    @Override
    @Transactional(readOnly = true)
    public List<DisplayAdminCustomerResponse> displayActive() {
        List<Customer> customers = repo.findByActiveTrue();

        // 1. Lấy ra danh sách các userId từ list customer
        List<UUID> userIds = customers.stream()
                .map(Customer::getUserId) // Lấy ra trường UUID userId
                .filter(Objects::nonNull)
                .distinct() // Tránh gọi API với các ID trùng lặp
                .toList();

        // 2. Gọi sang user service để lấy thông tin user theo batch
        Map<UUID, UserDto> usersByUserId = userClient.findByUserIds(userIds);

        // 3. Map kết quả cuối cùng
        return customers.stream()
                .map(c -> {
                    // Tra cứu trong map bằng userId của customer
                    UserDto userDto = usersByUserId.get(c.getUserId());
                    return displayCustomerMapper.display(c, userDto);
                })
                .toList();
    }

    //Find by Id
//    @Override
//    @Transactional(readOnly = true)
//    public Optional<Customer> findById(UUID id){
//        return repo.findById(id);
//    }

    //Find by Phone
    @Transactional(readOnly = true)
    @Override
    public Optional<Customer> findByPhone(String phone){return repo.findByPhone(phone);}

    //Find by Email
    @Transactional(readOnly = true)
    @Override
    public Optional<Customer> findByFullname(String fullname){return repo.findByFullname(fullname);}

    @Override
    @Transactional
    public Customer create(Customer input){
        return repo.save(input);
    }


    //Update Admin
    @Override
    @Transactional
// Trả về void vì không cần trả dữ liệu về
    public void update(UUID id, UpdateAdminCustomerRequest input) {
        // 1. Tìm Entity
        Customer currentCustomer = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        // 2. Dùng Mapper để áp dụng thay đổi từ DTO
        updateAdminCustomerMapper.updateAdminCustomer(currentCustomer, input);

        // 3. Lưu lại. Không cần gán vào biến mới vì `save` sẽ cập nhật `currentCustomer`
        repo.save(currentCustomer);
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
                .isActive(saved.getActive())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                // email sẽ được add sau nếu cần (từ User entity)
                .build();
    }


}
