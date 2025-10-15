package com.asm.ecommerce.auth.service;

import com.asm.ecommerce.auth.dto.UserDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface UserContractService {
    Map<UUID, UserDto>  findByCustomerIds(List<UUID> customerIds);
}
