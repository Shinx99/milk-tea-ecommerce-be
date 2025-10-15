package com.asm.ecommerce.auth.controller;

import com.asm.ecommerce.auth.dto.UserDto;
import com.asm.ecommerce.auth.service.UserContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/internal/users")
@RequiredArgsConstructor
public class InternalUserController {

    private final UserContractService userContractService;

    @PostMapping("/by-customer-ids")

    public Map<UUID, UserDto> findByCustomerIds(@RequestBody List<UUID> customerIds) {
        return userContractService.findByCustomerIds(customerIds);
    }
}
