package com.asm.ecommerce.customer.mapper.request.customer;

import com.asm.ecommerce.customer.domain.Customer;
import com.asm.ecommerce.customer.dto.request.customer.UpdateAdminCustomerRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UpdateAdminCustomerMapper {

    // SỬA LẠI: Bỏ ignoreByDefault = true
    // Chỉ giữ lại chiến lược xử lý giá trị null
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

    // Bỏ hết các @Mapping không cần thiết.
    // MapStruct sẽ tự động map các trường có tên giống nhau:
    // - dto.phone -> entity.phone
    // - dto.fullname -> entity.fullname
    // - dto.isActive -> entity.isActive
    // - dto.userId -> entity.userId
    // Nó cũng sẽ tự động bỏ qua các trường không có trong DTO như id, createdAt, user...
    void updateAdminCustomer(@MappingTarget Customer entity, UpdateAdminCustomerRequest dto);
}


