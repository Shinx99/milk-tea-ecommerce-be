package com.asm.ecommerce.payment.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    // FK sang orders.id
    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    // VNPAY, COD, MOMO...
    @Column(name = "provider", length = 50, nullable = false)
    private String provider;

    // Mã tham chiếu gửi sang VNPAY: vnp_TxnRef
    @Column(name = "transaction_ref", length = 100, nullable = false, unique = true)
    private String transactionRef;

    // PENDING / SUCCESS / FAILED / REFUNDED
    @Column(name = "status", length = 30, nullable = false)
    private String status;

    @Column(name = "amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "currency", length = 3, nullable = false)
    private String currency = "VND";

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "paid_at")
    private Instant paidAt;

    @Column(name = "refunded_at")
    private Instant refundedAt;

    // Trường đặc thù VNPAY
    @Column(name = "vnp_transaction_no", length = 50)
    private String vnpTransactionNo;

    @Column(name = "vnp_response_code", length = 10)
    private String vnpResponseCode;

    @Column(name = "vnp_bank_code", length = 20)
    private String vnpBankCode;

    @Column(name = "vnp_pay_date", length = 20)
    private String vnpPayDate;

    // Lưu full payload callback/IPN nếu muốn
   /* @Column(name = "payload_json", columnDefinition = "jsonb", nullable = true)
    private String payloadJson;*/

    @Column(name = "payload_json")
    private String payloadJson;

    @PrePersist
    void prePersist() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
        if (status == null) {
            status = "PENDING";
        }
        if (currency == null) {
            currency = "VND";
        }
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = Instant.now();
    }
}
