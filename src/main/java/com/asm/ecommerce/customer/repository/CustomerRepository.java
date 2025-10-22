package com.asm.ecommerce.customer.repository;

import com.asm.ecommerce.customer.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    //--------------------------------SEARCH----------------------------------------------

    //Tra cuu theo phone
    Optional<Customer> findByPhone(String phone);

    //Tra cuu theo fullname
    Optional<Customer> findByFullname(String fullname);

    //Tra cuu theo id -> thich hop cho ham update
    Optional<Customer> findById(UUID uuid);

    //--------------------------------CRUD------------------------------------------------

    //Hien thi tat ca - READ
    List<Customer> findAll();

    //Hien len danh sach customer con hoat dong - READ
    List<Customer> findByActiveTrue();

    //Create or Update record
    <S extends Customer> S save(S entity);

    //Soft Delete
    @Modifying
    @Query("""
            update Customer c
            set c.active = false,
                c.updatedAt = :now
            where c.id = :id and c.active = true
            """)
    int softDeleteById(@Param("id") UUID id, @Param("now")Instant now);
}
