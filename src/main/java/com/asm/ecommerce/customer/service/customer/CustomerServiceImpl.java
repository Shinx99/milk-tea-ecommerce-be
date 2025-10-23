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

    /**
     * Tìm và hiển thị thông tin chi tiết của một khách hàng duy nhất dựa trên userId.
     *
     * @param userId ID của user liên kết với khách hàng cần tìm.
     * @return Một đối tượng DisplayAdminCustomerResponse chứa thông tin khách hàng và người dùng.
     * @throws EntityNotFoundException nếu không tìm thấy khách hàng nào với userId tương ứng.
     */
    @Transactional(readOnly = true)
    @Override
    public DisplayAdminCustomerResponse displayByUserId(UUID userId) {

        // 1. Tìm một customer duy nhất trong DB bằng userId.
        // Sử dụng orElseThrow để xử lý ngay trường hợp không tìm thấy và trả về lỗi 404.
        Customer customer = repo.findByUserIdAndActiveTrue(userId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy khách hàng với userId: " + userId));

        // 2. Gọi sang user service để lấy thông tin của user đó.
        // Chúng ta có thể tận dụng lại hàm gọi batch với danh sách chỉ có một phần tử.
        UserDto userDto = null;
        try {
            Map<UUID, UserDto> usersMap = userClient.findByUserIds(List.of(userId));
            userDto = usersMap.get(userId);
        } catch (Exception e) {
        }
        return displayCustomerMapper.display(customer, userDto);
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


    // Update Admin
    @Transactional
    public DisplayAdminCustomerResponse update(UUID id, UpdateAdminCustomerRequest input) {
        Customer current = repo.findByUserIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        updateAdminCustomerMapper.updateAdminCustomer(current, input);

        Customer saved = repo.save(current); // save trả về entity đã được persist/merge
        UserDto userDto = null;
        try {
            UUID userId = saved.getUserId();
            if (userId != null) {
                Map<UUID, UserDto> usersMap = userClient.findByUserIds(List.of(userId));
                userDto = usersMap.get(userId);
            }
        } catch (Exception e) {
            // log và bỏ qua để không rollback giao dịch vì lỗi dịch vụ ngoài
        }
        return displayCustomerMapper.display(saved, userDto);
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
