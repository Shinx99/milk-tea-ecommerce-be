package com.asm.ecommerce.payment.service;

import com.asm.ecommerce.payment.domain.Payment;
import com.asm.ecommerce.payment.dto.CreatePaymentResponseDto;
import com.asm.ecommerce.payment.dto.PaymentResultDto;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface PaymentService {

    // 1. Tạo thanh toán (VNPAY) sau khi đã có order
    CreatePaymentResponseDto createVNPayPayment(UUID orderId, String clientIp);

    // 2. Xử lý callback / IPN từ VNPAY (map vnp_ params -> cập nhật Payment + Order)
    void handleVNPayCallback(Map<String, String> vnpParams);

    // 3. Lấy trạng thái thanh toán cho FE (màn payment-result hoặc chi tiết đơn)
    PaymentResultDto getPaymentResultByOrderId(UUID orderId);

    // (Optional) 4. Tạo bản ghi payment cho COD, không đi qua VNPAY
    PaymentResultDto createCodPayment(UUID orderId);

    // Lấy trạng thái thanh toán cho FE
    PaymentResultDto getPaymentResultByOrderCode(String orderCode);

    // todo: ==== invoice PDF ====
    Optional<Payment> getPaymentByOrderId(UUID orderId);

}
