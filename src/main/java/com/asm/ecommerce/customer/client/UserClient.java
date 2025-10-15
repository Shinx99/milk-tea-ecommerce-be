package com.asm.ecommerce.customer.client;

import com.asm.ecommerce.auth.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(
        name = "user-client",
        url = "${user.service.url:http://localhost:8080}",
        path = "/internal/users"
)
public interface UserClient {
    @PostMapping("/by-customer-ids")
    Map<UUID, UserDto> findByCustomerIds(@RequestBody List<UUID> ids);
}
