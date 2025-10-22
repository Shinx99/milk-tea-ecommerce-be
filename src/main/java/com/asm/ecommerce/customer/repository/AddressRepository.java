package com.asm.ecommerce.customer.repository;

import com.asm.ecommerce.customer.domain.Address;
import com.asm.ecommerce.customer.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {

    //Display--------------------------------------------------------------------------------------------------------------

    //Hien thi tat ca - Read - Admin
    @Query("SELECT a FROM Address a JOIN FETCH a.customer")
    List<Address> findAllWithCustomer();

    @Query("SELECT a FROM Address a JOIN FETCH a.customer WHERE a.active = true")
     List<Address> findAllWithCustomerByActiveTrue();


    //Display--------------------------------------------------------------------------------------------------------------

    //---------------------------------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------------------------------

    //CRUD----------------------------------------------------------------------------------------------------------------

    // Lấy ID địa chỉ mặc định theo customerId
    @Query("SELECT a.id FROM Address a WHERE a.customer.id = :customerId AND a.isDefault = true AND a.active = true")
    Optional<UUID> findDefaultAddressIdByCustomerId(@Param("customerId") UUID customerId);

    // Bỏ cờ mặc định theo addressId
    @Modifying
    @Query("UPDATE Address a SET a.isDefault = false WHERE a.id = :id AND a.isDefault = true AND a.active = true")
    int clearDefaultAddressForCustomer(@Param("id") UUID addressId);

    // Lấy customerId từ addressId (phục vụ update)
    @Query("SELECT a.customer.id FROM Address a WHERE a.id = :addressId AND a.active = true")
    Optional<UUID> findCustomerIdByAddressId(@Param("addressId") UUID addressId);


    @Query(value = """
        SELECT a.id
        FROM addresses a
        WHERE a.customer_id = :customerId
        AND a.is_active = true
        AND a.id <> :excludeId
        ORDER BY a.id DESC
        LIMIT 1
""", nativeQuery = true)
    Optional<UUID> findOneActiveAddressIdForCustomer(@Param("customerId") UUID customerId,
                                                     @Param("excludeId")  UUID excludeId);


    @Modifying
    @Query("UPDATE Address a SET a.isDefault = true WHERE a.id = :id AND a.active = true")
    int setDefaultbyId(@Param("id") UUID id);

    //Dung cho ham update
    Optional<Address> findById(UUID uuid);

    //Create or update record
    <S extends Address> S save(S entity);

    //Soft delete
    @Modifying
    @Query("""
        update Address a
        set a.active = false
        where a.id = :id and a.active = true
        """)
    int softDeleteById(@Param("id") UUID id);

    //CRUD----------------------------------------------------------------------------------------------------------------

}
