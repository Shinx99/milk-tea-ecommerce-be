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
import com.asm.ecommerce.shared.dto.ApiResponse;
import com.asm.ecommerce.shared.dto.PageResponse;
import com.asm.ecommerce.shared.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Transactional(readOnly = true)
    @Override
    public ApiResponse<PageResponse<DisplayAdminCustomerResponse>> displayAll(String keyword, Pageable pageable) {
        Page<Customer> customers = repo.findAll(keyword, pageable);

        // 1. Lấy ra danh sách userId không trùng
        List<UUID> userIds = customers.stream()
                .map(Customer::getUserId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        // 2. Gọi sang user service để lấy thông tin user theo batch
        Map<UUID, UserDto> usersByUserId = userClient.findByUserIds(userIds);

        // 3. Map danh sách customer + user sang DisplayAdminCustomerResponse
        List<DisplayAdminCustomerResponse> content = customers.stream()
                .map(c -> {
                    UserDto userDto = usersByUserId.get(c.getUserId());
                    return displayCustomerMapper.display(c, userDto);
                })
                .toList();

        // 4. Build PageResponse với dữ liệu đã map, giữ nguyên phân trang từ customers
        PageResponse<DisplayAdminCustomerResponse> pageResponse = PageResponse.<DisplayAdminCustomerResponse>builder()
                .content(content)
                .pageNumber(customers.getNumber())
                .pageSize(customers.getSize())
                .totalPages(customers.getTotalPages())
                .totalElements(customers.getTotalElements())
                .last(customers.isLast())
                .build();

        // 5. Trả về ApiResponse bọc PageResponse
        return ApiResponse.<PageResponse<DisplayAdminCustomerResponse>>builder()
                .success(true)
                .message("Customers retrieved successfully")
                .data(pageResponse)
                .build();
    }



    //Display active customer
    @Override
    @Transactional(readOnly = true)
    public ApiResponse<PageResponse<DisplayAdminCustomerResponse>> displayActive(Pageable pageable) {

        Page<Customer> customers = repo.findByActiveTrue(pageable);

        // 1. Lấy ra danh sách các userId từ list customer
        List<UUID> userIds = customers.stream()
                .map(Customer::getUserId) // Lấy ra trường UUID userId
                .filter(Objects::nonNull)
                .distinct() // Tránh gọi API với các ID trùng lặp
                .toList();

        // 2. Gọi sang user service để lấy thông tin user theo batch
        Map<UUID, UserDto> usersByUserId = userClient.findByUserIds(userIds);

        // 3. Map kết quả cuối cùng
        List<DisplayAdminCustomerResponse> content = customers.stream()
                .map(c -> {
                    // Tra cứu trong map bằng userId của customer
                    UserDto userDto = usersByUserId.get(c.getUserId());
                    return displayCustomerMapper.display(c, userDto);
                })
                .toList();

        // 4. Build PageResponse voi du lieu da map, giu nguyen phan trang tu customers
        PageResponse<DisplayAdminCustomerResponse> pageResponse = PageResponse.<DisplayAdminCustomerResponse>builder()
                .content(content)
                .pageNumber(customers.getNumber())
                .pageSize(customers.getSize())
                .totalPages(customers.getTotalPages())
                .totalElements(customers.getTotalElements())
                .last(customers.isLast())
                .build();

        return ApiResponse.<PageResponse<DisplayAdminCustomerResponse>>builder()
                .success(true)
                .message("Active Customers retrieved successfully")
                .data(pageResponse)
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse<DisplayAdminCustomerResponse> displayByUserId(UUID userId) {

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
        DisplayAdminCustomerResponse response = displayCustomerMapper.display(customer, userDto);

        return ApiResponse.<DisplayAdminCustomerResponse>builder()
                .success(true)
                .message("Display By UserId successfully")
                .data(response)
                .build();
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


    // Update customer --> Customer Page
    @Transactional
    public ApiResponse<DisplayAdminCustomerResponse> update(UUID id, UpdateAdminCustomerRequest input) {
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
        }
        DisplayAdminCustomerResponse response = displayCustomerMapper.display(saved, userDto);

        return ApiResponse.<DisplayAdminCustomerResponse>builder()
                .success(true)
                .message("Customers updated successfully")
                .data(response)
                .build();
    }

    // Update customer --> Admin Page
    @Transactional
    public ApiResponse<DisplayAdminCustomerResponse> updateAdmin(UUID customerId,
                                                                 UpdateAdminCustomerRequest input) {
        // 1. Tìm customer theo customerId
        Customer current = repo.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        // 2. Map dữ liệu update từ DTO vào entity hiện tại
        updateAdminCustomerMapper.updateAdminCustomer(current, input);

        // 3. Lưu lại
        Customer saved = repo.save(current);

        // 4. Lấy thêm thông tin user từ service user (như method trên)
        UserDto userDto = null;
        try {
            UUID userId = saved.getUserId();
            if (userId != null) {
                Map<UUID, UserDto> usersMap = userClient.findByUserIds(List.of(userId));
                userDto = usersMap.get(userId);
            }
        } catch (Exception e) {
        }

        // 5. Map sang response cho admin
        DisplayAdminCustomerResponse response = displayCustomerMapper.display(saved, userDto);

        return ApiResponse.<DisplayAdminCustomerResponse>builder()
                .success(true)
                .message("Customer updated successfully")
                .data(response)
                .build();
    }

    // Soft delete cho Admin
    @Override
    @Transactional
    public void softDelete(UUID customerId){

        int updated = repo.softDeleteById(customerId, Instant.now());
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

    //=========== Cart ============
    @Override
    public UUID getCustomerIdByUserId(UUID userId) {
        return repo.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No customer found for userId: "+userId))
                .getId();
    }

    //========== OrderInvoce ==========
    @Override
    public UUID getUserIdByCustomerId(UUID customerId) {
        Customer customer = repo.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        return customer.getUserId(); // field FK sang bảng user
    }




    //toDo: Vuong -> OrderAdmin export service cho feature Order
    @Transactional(readOnly = true)
    @Override
    public DisplayAdminCustomerResponse getOrderCustomer(UUID customerId) {
        Customer customer = repo.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        UserDto userDto = null;
        try {
            Map<UUID, UserDto> usersMap = userClient.findByUserIds(
                    List.of(customer.getUserId()));
            userDto = usersMap.get(customer.getUserId());
        } catch (Exception e) {
            log.warn("Failed to fetch user {} for customer {}: {}",
                    customer.getUserId(), customerId, e.getMessage());
        }
        return displayCustomerMapper.display(customer, userDto);
    }


}
