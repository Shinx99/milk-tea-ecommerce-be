package com.asm.ecommerce.payment.controller;


import com.asm.ecommerce.payment.config.VNPayUtil;
import com.asm.ecommerce.payment.dto.CreatePaymentRequestDto;
import com.asm.ecommerce.payment.dto.CreatePaymentResponseDto;
import com.asm.ecommerce.payment.dto.PaymentResultDto;
import com.asm.ecommerce.payment.service.PaymentService;
import com.asm.ecommerce.shared.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;
    private final VNPayUtil vnPayUtil;

    /**
     * Tạo thanh toán VNPay cho 1 order (FE gọi sau khi user chọn VNPAY)
     */
    @PostMapping("/vnpay/create")
    public ResponseEntity<ApiResponse<CreatePaymentResponseDto>> createVNPayPayment(
            @RequestBody CreatePaymentRequestDto request,
            HttpServletRequest httpRequest
    ) {
        String clientIp = getClientIpAddress(httpRequest);
        log.info("Create VNPay payment for order: {}, ip={}", request.getOrderId(), clientIp);

        CreatePaymentResponseDto dto =
                paymentService.createVNPayPayment(request.getOrderId(), clientIp);

        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    /**
     * FE lấy kết quả thanh toán của 1 order (màn payment result / chi tiết đơn)
     */

    @GetMapping("/by-code/{orderCode}/result")
    public ResponseEntity<ApiResponse<PaymentResultDto>> getPaymentResult(
            @PathVariable String orderCode
    ) {
        PaymentResultDto dto = paymentService.getPaymentResultByOrderCode(orderCode);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }


    /**
     * Tạo payment COD (nếu user chọn ship COD)
     */
    @PostMapping("/cod/create")
    public ResponseEntity<ApiResponse<PaymentResultDto>> createCodPayment(
            @RequestBody CreatePaymentRequestDto request
    ) {
        PaymentResultDto dto = paymentService.createCodPayment(request.getOrderId());
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    /**
     * VNPay return-url: user được redirect về sau khi thanh toán
     * → Ở đây thường chỉ xử lý nhẹ và redirect FE, logic chính nằm ở IPN
     */
    @GetMapping("/vnpay/return")
    public ResponseEntity<Void> vnpayReturn(
            @RequestParam Map<String, String> params
    ) {
        log.info("VNPay return received: {}", params);
        // tuỳ nghiệp vụ: có thể chỉ log, hoặc cũng gọi handleVNPayCallback nếu bạn
        // không muốn tách IPN. Thực tế nên để logic chính ở IPN.
        //1. (Optional) validate chữ ký
        boolean valid = vnPayUtil.validateSignature((params));
        if(!valid){
            //chuyển sang trang báo lỗi chung
            URI uri = URI.create("http://localhost:5173/payment-result?status=INVALID_SIGNATURE");
            return ResponseEntity.status(302).location(uri).build();
        }

        // 2. Lấy orderCode / transactionRef để FE tra kết quả
        String orderCode = params.get("vnp_TxnRef");

        // 3. Redirect sang trang Vue hiển thị kết quả
        URI uri = URI.create("http://localhost:5173/payment-result?orderCode="+orderCode);
        return ResponseEntity.status(302).location(uri).build();

        // Ví dụ: chỉ validate chữ ký, còn lại để IPN xử lý.
       /* boolean valid = vnPayUtil.validateSignature(params);
        if (!valid) {
            log.error("Invalid VNPay signature in return");
        }*/

        // FE sẽ đọc query ở URL trực tiếp (code, message, orderCode) nếu bạn redirect từ gateway,
        // nên controller này có thể không cần redirect nữa nếu gateway đã làm.

        //return ResponseEntity.ok().build();
    }

    /**
     * VNPay IPN: server-to-server, nơi thực sự cập nhật trạng thái Payment + Order
     * Trả về JSON đúng format VNPAY yêu cầu, không bọc ApiResponse.
     */
    @RequestMapping(value = "/vnpay/ipn", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> vnpayIpn(@RequestParam Map<String, String> params) {
        log.info("VNPay IPN received: {}", params);
        try {
            boolean valid = vnPayUtil.validateSignature(params);
            log.info("[VNPay][IPN] signature valid = {}", valid);

            if (!valid) {
                log.error("Invalid VNPay signature in IPN: {}", params);
                return ResponseEntity.ok("{\"RspCode\":\"97\",\"Message\":\"Invalid signature\"}");
            }

            paymentService.handleVNPayCallback(params);
            log.info("[VNPay][IPN] handleVNPayCallback OK for vnp_TxnRef={}", params.get("vnp_TxnRef"));
            return ResponseEntity.ok("{\"RspCode\":\"00\",\"Message\":\"Confirm Success\"}");

        } catch (Exception e) {
            log.error("[VNPay][IPN] Error processing IPN, params={}", params, e);
            return ResponseEntity.ok("{\"RspCode\":\"99\",\"Message\":\"Unknown error\"}");
        }

    }

    // ===== Helper =====

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }


}
