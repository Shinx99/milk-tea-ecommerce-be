package com.asm.ecommerce.auth.repository;

import com.asm.ecommerce.auth.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByRole(String role);

    boolean existsByRole(String role);


}
