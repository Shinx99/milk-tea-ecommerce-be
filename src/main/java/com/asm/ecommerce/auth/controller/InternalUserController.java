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

    @PostMapping("/by-user-ids")
    public Map<UUID, UserDto> findByUserIds(@RequestBody List<UUID> userIds) {
        return userContractService.findByUserIds(userIds);
    }
}
