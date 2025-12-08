package com.asm.ecommerce.payment.repository;

import com.asm.ecommerce.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    // Dùng khi xử lý callback/IPN: map từ vnp_TxnRef -> Payment
    Optional<Payment> findByTransactionRef(String transactionRef);

    // Nếu muốn lấy payment mới nhất theo order Id
    Optional<Payment> findFirstByOrderIdOrderByCreatedAtDesc(UUID orderId);

    // Lấy payment theo order-code (transactionRef = orderCode)
    Optional<Payment> findFirstByTransactionRefOrderByCreatedAtDesc(String transactionRef);

}
