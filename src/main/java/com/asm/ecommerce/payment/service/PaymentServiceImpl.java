package com.asm.ecommerce.payment.service;

import com.asm.ecommerce.order.dto.payment.OrderSummaryDto;
import com.asm.ecommerce.order.service.InvoicePdfService;
import com.asm.ecommerce.order.service.OrderService;
import com.asm.ecommerce.payment.config.VNPayUtil;
import com.asm.ecommerce.payment.domain.Payment;
import com.asm.ecommerce.payment.dto.CreatePaymentResponseDto;
import com.asm.ecommerce.payment.dto.PaymentResultDto;
import com.asm.ecommerce.payment.mapper.PaymentMapper;
import com.asm.ecommerce.payment.repository.PaymentRepository;
import com.asm.ecommerce.shared.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService{

    private final PaymentRepository paymentRepository;
    private final OrderService orderService;
    private final VNPayUtil vnPayUtil;
    private final PaymentMapper paymentMapper;
    private final InvoicePdfService invoicePdfService;
    private final EmailService emailService;

    @Override
    @Transactional
    public CreatePaymentResponseDto createVNPayPayment(UUID orderId, String clientIp) {

        // 1. Lấy thông tin order qua public service
        OrderSummaryDto order = orderService.getOrderForPayment(orderId);

        if (!"pending".equalsIgnoreCase(order.getStatus())) {
            throw new RuntimeException("Order is not in PENDING status");
        }

        BigDecimal amount = order.getTotal();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Order amount is invalid");
        }

        // 2. Tạo bản ghi Payment
        Payment payment = new Payment();
        payment.setOrderId(order.getId());
        payment.setProvider("VNPAY");
        payment.setStatus("PENDING");
        payment.setAmount(amount);
        payment.setCurrency(order.getCurrency() != null ? order.getCurrency() : "VND");

        // transactionRef: dùng orderCode hoặc sinh riêng, nhưng phải trùng với vnp_TxnRef
        String transactionRef = order.getOrderCode();
        payment.setTransactionRef(transactionRef);

        payment = paymentRepository.save(payment);

        try {
            // 3. Tạo URL thanh toán VNPay
            String paymentUrl = vnPayUtil.createPayment(
                    transactionRef,
                    amount.longValue(),
                    "Thanh toan don hang " + order.getOrderCode(),
                    "other",
                    clientIp
            );

            // 4. Map DTO trả về cho FE
            CreatePaymentResponseDto dto = paymentMapper.toCreatePaymentResponseDto(order, payment);
            dto.setPaymentUrl(paymentUrl);
            dto.setStatus(payment.getStatus());
            dto.setMessage("Tạo yêu cầu thanh toán thành công");
            return dto;

        } catch (Exception e) {
            log.error("Error creating VNPay payment", e);
            payment.setStatus("FAILED");
            paymentRepository.save(payment);
            throw new RuntimeException("Không tạo được URL thanh toán VNPAY");
        }

    }

    @Override
    @Transactional
    public void handleVNPayCallback(Map<String, String> vnpParams) {
        log.info("Handling VNPay callback: {}", vnpParams);

        // 1. Validate chữ ký
        boolean valid = vnPayUtil.validateSignature(vnpParams);
        log.info("[VNPay][CALLBACK] signature valid={}", valid);

        if (!valid) {
            log.error("[VNPay][CALLBACK] invalid signature, params={}", vnpParams);
            log.error("Invalid VNPay signature");
            throw new RuntimeException("Invalid VNPay signature");
        }

        String responseCode = vnpParams.get("vnp_ResponseCode");
        String transactionRef = vnpParams.get("vnp_TxnRef");
        String vnpAmountStr = vnpParams.get("vnp_Amount");

        log.info("[VNPay][CALLBACK] code={}, txnRef={}, amountRaw={}",
                responseCode, transactionRef, vnpAmountStr);


        // 2. Tìm payment theo transactionRef
        Payment payment = paymentRepository.findByTransactionRef(transactionRef)
                .orElseThrow(() -> new RuntimeException("Payment not found for ref: " + transactionRef));

        // 3. Kiểm tra số tiền (optional nhưng nên làm)
        if (vnpAmountStr != null) {
            BigDecimal vnpAmount = new BigDecimal(vnpAmountStr).divide(BigDecimal.valueOf(100));

            log.info("[VNPay][CALLBACK] compare amount payment={} vs vnp={}",
                    payment.getAmount(), vnpAmount);

            if (payment.getAmount().compareTo(vnpAmount) != 0) {
                log.error("Amount mismatch: payment={}, vnp={}", payment.getAmount(), vnpAmount);
                throw new RuntimeException("Invalid amount");
            }
        }

        // 4. Cập nhật thông tin VNPay vào Payment
        payment.setVnpResponseCode(responseCode);
        payment.setVnpTransactionNo(vnpParams.get("vnp_TransactionNo"));
        payment.setVnpBankCode(vnpParams.get("vnp_BankCode"));
        payment.setVnpPayDate(vnpParams.get("vnp_PayDate"));
        payment.setPayloadJson(vnpParams.toString());
        payment.setUpdatedAt(Instant.now());

        if ("00".equals(responseCode)) {
            payment.setStatus("SUCCESS");
            payment.setPaidAt(Instant.now());
            paymentRepository.save(payment);

            // 5. Đánh dấu đơn đã thanh toán qua public service
            orderService.markOrderPaid(payment.getOrderId());
            log.info("Payment SUCCESS for order {}, transaction {}", payment.getOrderId(), payment.getVnpTransactionNo());
        } else {
            payment.setStatus("FAILED");
            paymentRepository.save(payment);

            orderService.markOrderPaymentFailed(payment.getOrderId());
            log.warn("Payment FAILED for order {}, code {}", payment.getOrderId(), responseCode);
        }
    }

    @Override
    @Transactional
    public PaymentResultDto getPaymentResultByOrderId(UUID orderId) {
        OrderSummaryDto order = orderService.getOrderForPayment(orderId);

        Payment payment = paymentRepository
                .findFirstByOrderIdOrderByCreatedAtDesc(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found for order: " + orderId));

        PaymentResultDto dto = paymentMapper.toPaymentResultDto(order, payment);
        dto.setMessage("Trạng thái thanh toán: " + payment.getStatus());
        return dto;
    }

    @Override
    @Transactional
    public PaymentResultDto createCodPayment(UUID orderId) {
        OrderSummaryDto order = orderService.getOrderForPayment(orderId);

        BigDecimal amount = order.getTotal();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Order amount is invalid");
        }

        Payment payment = new Payment();
        payment.setOrderId(order.getId());
        payment.setProvider("COD");
        payment.setStatus("PENDING"); // hoặc "SUCCESS" tuỳ nghiệp vụ
        payment.setAmount(amount);
        payment.setCurrency(order.getCurrency() != null ? order.getCurrency() : "VND");
        payment.setTransactionRef(order.getOrderCode());

        payment = paymentRepository.save(payment);

        PaymentResultDto dto = paymentMapper.toPaymentResultDto(order, payment);
        dto.setMessage("Đã ghi nhận thanh toán COD");
        return dto;
    }

    @Override
    public PaymentResultDto getPaymentResultByOrderCode(String orderCode) {

        // 1. Tìm order theo orderCode (lấy thông tin đơn)
        OrderSummaryDto order = orderService.getOrderForPaymentByCode(orderCode);

        // 2. Tìm payment mới nhất theo transactionRef == orderCode
        Payment payment = paymentRepository
                .findFirstByTransactionRefOrderByCreatedAtDesc(orderCode)
                .orElseThrow(() -> new RuntimeException(
                        "Payment not found for orderCode: " + orderCode
                ));

        // 3. Map sang DTO
        PaymentResultDto dto = paymentMapper.toPaymentResultDto(order, payment);
        dto.setMessage("Trạng thái thanh toán: " + payment.getStatus());
        return dto;
    }

    // todo: ==== invoice pdf ====
    @Override
    public Optional<Payment> getPaymentByOrderId(UUID orderId) {
        return Optional.ofNullable(paymentRepository.findByOrderId(orderId)
                .orElse(null));
    }
}
