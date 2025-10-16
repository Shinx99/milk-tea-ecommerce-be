package com.asm.ecommerce.auth.repository;

import com.asm.ecommerce.auth.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    // Tìm theo tên role
    Optional<Role> findByRole(String role);

    // Tìm theo tên role (case insensitive)
    Optional<Role> findByRoleIgnoreCase(String role);

    // Kiểm tra role tồn tại
    boolean existsByRole(String role);

    // Tìm các role active
    List<Role> findByIsActiveTrue();

    // Tìm theo description
    List<Role> findByDescriptionContaining(String keyword);

    // Custom query nếu cần
    @Query("SELECT r FROM Role r WHERE LOWER(r.role) = LOWER(:roleName)")
    Optional<Role> findByRoleName(@Param("roleName") String roleName);


}
