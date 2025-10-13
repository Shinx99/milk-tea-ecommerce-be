package com.asm.ecommerce.auth.config;

import com.asm.ecommerce.auth.domain.Role;
import com.asm.ecommerce.auth.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.OffsetDateTime;
import java.util.UUID;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RoleSeeder {

    @Bean
    CommandLineRunner seedRoles(RoleRepository roleRepository) {
        return args -> {
            //Check để tránh duplicate
            if (roleRepository.count() == 0) {
                log.info("Seeding roles...");
                
                createRole(roleRepository, "ADMIN", "Administrator with full access");
                createRole(roleRepository, "customer", "Regular customer");
                createRole(roleRepository, "STAFF", "Staff member");
                
                log.info("Roles seeded successfully");
            } else {
                log.info("Roles already exist, skipping seed");
            }
        };
    }

    private void createRole(RoleRepository repository, String roleName, String description) {
        Role role = Role.builder()
                .id(UUID.randomUUID())
                .role(roleName)
                .description(description)
                .isActive(true)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
        repository.save(role);
    }
}
