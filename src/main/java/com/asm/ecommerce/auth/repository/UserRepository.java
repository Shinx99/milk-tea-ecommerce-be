package com.asm.ecommerce.auth.repository;

import com.asm.ecommerce.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u JOIN FETCH u.role WHERE u.email = :email")
    Optional<User> findByEmailWithRole(String email);

    @Query("SELECT u FROM User u WHERE u.role.role = :roleName")
    List<User> findByRoleName(String roleName);

    List<User> findByIsActive(Boolean isActive);

    //Vuong edit
    // Co the khong can nua
    //List<User> findByCustomerIdIn(Collection<UUID> customerIds);
}
