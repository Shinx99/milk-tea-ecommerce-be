package com.asm.ecommerce.customer.repository;

import com.asm.ecommerce.customer.domain.CustomerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<CustomerModel, UUID> {

    //--------------------------------SEARCH----------------------------------------------

    //Tra cuu theo phone
    Optional<CustomerModel> findByPhone(String phone);

    //Tra cuu theo fullname
    Optional<CustomerModel> findByFullname(String fullname);

    //Tra cuu theo id -> thich hop cho ham update
    Optional<CustomerModel> findById(UUID uuid);

    //--------------------------------CRUD------------------------------------------------

    //Hien thi tat ca - READ
    List<CustomerModel> findAll();

    //Hien len danh sach customer con hoat dong - READ
    List<CustomerModel> findByIsActiveTrue();

    //Create or Update record
    <S extends CustomerModel> S save(S entity);

    //Soft Delete
    @Modifying
    @Query("""
            update CustomerModel c
            set c.isActive = false,
                c.updatedAt = :now
            where c.id = :id and c.isActive = true
            """)
    int softDeleteById(@Param("id") UUID id, @Param("now")Instant now);
}
