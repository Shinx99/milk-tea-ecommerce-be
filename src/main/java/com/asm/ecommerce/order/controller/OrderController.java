package com.asm.ecommerce.order.controller;

import com.asm.ecommerce.order.dto.request.CreateOrderRequestDto;
import com.asm.ecommerce.order.dto.response.OrderDetailDto;
import com.asm.ecommerce.order.service.OrderService;
import com.asm.ecommerce.shared.dto.ApiResponse;
import com.asm.ecommerce.shared.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    /**
     * Checkout: tạo order từ cart hiện tại của user
     * userId có thể lấy từ JWT (SecurityContext) hoặc path/query tuỳ cách bạn đang làm.
     */
    @PostMapping("/checkout")
    public ResponseEntity<ApiResponse<OrderDetailDto>> placeOrderFromCart(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody CreateOrderRequestDto request
    ) {
        UUID userId = userPrincipal.getId();
        log.info("Adding item to cart for user: {}", userId);

        OrderDetailDto order = orderService.placeOrderFromCart(userId, request);
        return ResponseEntity.ok(ApiResponse.success(order));
    }

    /**
     * Lấy chi tiết 1 order để hiển thị "hóa đơn"
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderDetailDto>> getOrderById(
            @PathVariable UUID orderId
    ) {
        OrderDetailDto order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(ApiResponse.success(order));
    }
}
