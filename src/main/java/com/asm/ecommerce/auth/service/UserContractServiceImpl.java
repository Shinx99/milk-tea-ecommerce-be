package com.asm.ecommerce.auth.service;

import com.asm.ecommerce.auth.domain.User;
import com.asm.ecommerce.auth.dto.UserDto;
import com.asm.ecommerce.auth.mapper.UserMapper;
import com.asm.ecommerce.auth.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserContractServiceImpl implements UserContractService {

    private final UserRepository repo;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public Map<UUID, UserDto> findByCustomerIds(List<UUID> customerIds) {
        if (customerIds == null || customerIds.isEmpty()) return Map.of();

        List<UUID> ids = customerIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        if (ids.isEmpty()) return Map.of();

        List<User> users = repo.findByCustomerIdIn(ids);

        return users.stream()
                .map(userMapper::toDto) // dùng instance mapper, không static [web:66][web:31]
                .filter(dto -> dto.getCustomerId() != null) // tránh null key cho toMap [web:84]
                .collect(Collectors.toMap(
                        UserDto::getCustomerId,         // key: customerId
                        Function.identity(),            // value: chính DTO
                        (a, b) -> a,    // khóa trùng: giữ a (hoặc đổi thành b) [web:84]
                        LinkedHashMap::new              // giữ thứ tự chèn nếu cần [web:90]
                ));
    }
}
