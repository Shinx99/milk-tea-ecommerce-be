package com.asm.ecommerce.order.service;

import com.asm.ecommerce.order.dto.payment.OrderSummaryDto;
import com.asm.ecommerce.order.dto.request.CreateOrderRequestDto;
import com.asm.ecommerce.order.dto.request.OrderItemRequestDto;
import com.asm.ecommerce.order.dto.response.OrderDetailDto;
import com.asm.ecommerce.order.dto.response.OrderItemDto;
import com.asm.ecommerce.shared.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderService {


    // checkout: tạo order từ cart hiện tại của user
    OrderDetailDto placeOrderFromCart(UUID userId, CreateOrderRequestDto request);

    // xem chi tiết 1 order
    OrderDetailDto getOrderById(UUID orderId);


    OrderSummaryDto getOrderForPayment(UUID orderId);   // chứa id, orderCode, total, currency, status
    void markOrderPaid(UUID orderId);
    void markOrderPaymentFailed(UUID orderId);


}
