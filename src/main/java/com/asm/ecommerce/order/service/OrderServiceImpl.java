package com.asm.ecommerce.order.service;

import com.asm.ecommerce.cart.dto.order.CartItemDto;
import com.asm.ecommerce.cart.service.CartService;
import com.asm.ecommerce.customer.dto.response.address.DisplayAdminAddressResponse;
import com.asm.ecommerce.customer.dto.response.customer.DisplayAdminCustomerResponse;
import com.asm.ecommerce.customer.service.address.AddressService;
import com.asm.ecommerce.customer.service.customer.CustomerService;
import com.asm.ecommerce.order.domain.Order;
import com.asm.ecommerce.order.domain.OrderItem;
import com.asm.ecommerce.order.dto.payment.OrderSummaryDto;
import com.asm.ecommerce.order.dto.request.CreateOrderRequestDto;
import com.asm.ecommerce.order.dto.response.AdminOrderDto;
import com.asm.ecommerce.order.dto.response.OrderDetailDto;
import com.asm.ecommerce.order.dto.response.OrderItemDto;
import com.asm.ecommerce.order.mapper.AdminOrderMapper;
import com.asm.ecommerce.order.mapper.OrderMapper;
import com.asm.ecommerce.order.mapper.OrderRequestMapper;
import com.asm.ecommerce.order.repository.OrderRepository;
import com.asm.ecommerce.product.domain.Product;
import com.asm.ecommerce.product.repository.ProductRepository;
import com.asm.ecommerce.shared.dto.ApiResponse;
import com.asm.ecommerce.shared.dto.PageResponse;
import com.asm.ecommerce.shared.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderRequestMapper orderRequestMapper;
    private final OrderMapper orderMapper;
    private final CartService cartService;

    // Vuong---------------------------------------
    private final AdminOrderMapper adminOrderMapper;
    private final CustomerService customerService;
    private final AddressService addressService;
    private final ProductRepository productRepository;
    // Vuong---------------------------------------

    @Override
    @Transactional
    public OrderDetailDto placeOrderFromCart(UUID userId, CreateOrderRequestDto request) {

        // 1. Lấy các cart item active của user
        List<CartItemDto> cartItems = cartService.getActiveItemsForOrder(userId);
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Giỏ hàng đang trống, không thể tạo đơn");
        }

        // 2. Map request -> Order entity (chưa set tiền, status)
        Order order = orderRequestMapper.toOrder(request);
        log.info("Order after mapper: items is null? {}", order.getItems() == null);

        // Fix null items
        if (order.getItems() == null) {
            order.setItems(new ArrayList<>());
            log.info("Manually initialized items list");
        }

        // get customerId by userId
        UUID customerId = customerService.getCustomerIdByUserId(userId);
        order.setCustomerId(customerId);
        order.setStatus("pending");
        order.setPlacedAt(Instant.now());

        // 3. Tính subtotal, total, tạo OrderItem từ CartItemDto
        BigDecimal subtotal = BigDecimal.ZERO;

        for (CartItemDto cartItem : cartItems) {
            OrderItem item = new OrderItem();
            item.setProductId(cartItem.getProductId());
            item.setQuantity(cartItem.getQuantity());
            item.setPrice(cartItem.getUnitPrice());
            //item.setLineTotal(cartItem.getLineTotal());
            item.setSizeCategoryId(cartItem.getSizeCategoryId());
            item.setSugarCategoryId(cartItem.getSugarCategoryId());
            item.setIceCategoryId(cartItem.getIceCategoryId());
            item.setTemperatureCategoryId(cartItem.getTemperatureCategoryId());

            order.addItem(item); // helper method trong Order

            subtotal = subtotal.add(cartItem.getLineTotal());
        }

        order.setSubtotal(subtotal);
        order.setDiscountTotal(BigDecimal.ZERO);
        order.setTaxTotal(BigDecimal.ZERO);
        order.setShippingFee(BigDecimal.ZERO);
        order.setTotal(subtotal);
        order.setCurrency("VND");

        // 4. Tạo mã đơn (simple)
        order.setOrderCode("ORD-" + System.currentTimeMillis());

        // 5. Lưu order
        order = orderRepository.save(order);

        // 6. Đổi trạng thái cart thành abandoned
        cartService.markCartAsAbandonedAfterOrder(userId);

        // 7. Map entity -> OrderDetailDto trả về cho client
        return orderMapper.toOrderDetailDto(order);
    }

    @Override
    @Transactional
    public OrderDetailDto getOrderById(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found with id: " + orderId));

        return orderMapper.toOrderDetailDto(order);
    }


    // todo: ===== Payment =====
    @Override
    @Transactional
    public OrderSummaryDto getOrderForPayment(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found with id: " + orderId));

        log.info("getOrderForPayment orderId={}", orderId);


        // Nếu cần, có thể validate trạng thái
        // if (!"pending".equalsIgnoreCase(order.getStatus())) { ... }

        return orderMapper.toOrderSummaryDto(order);
    }

    @Override
    public OrderSummaryDto getOrderForPaymentByCode(String orderCode) {

        Order order = orderRepository.findByOrderCode(orderCode)
                .orElseThrow(() -> new RuntimeException(
                        "Order not found with code: " + orderCode));

        // Nếu bạn đã có mapper Order -> OrderSummaryDto
        return orderMapper.toOrderSummaryDto(order);
    }

    @Override
    @Transactional
    public void markOrderPaid(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found with id: " + orderId));

        // Chỉ cho phép chuyển từ pending → paid
        if (!"pending".equalsIgnoreCase(order.getStatus())) {
            return; // hoặc throw BusinessException tuỳ bạn
        }

        order.setStatus("paid");
        order.setCompletedAt(Instant.now()); // nếu bạn có field này
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void markOrderPaymentFailed(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found with id: " + orderId));

        // Nếu đã paid rồi thì không đổi nữa
        if ("paid".equalsIgnoreCase(order.getStatus())) {
            return;
        }

        order.setStatus("payment_failed");
        orderRepository.save(order);
    }

    //toDo: Vuong -> AdminOrder
    @Transactional
    @Override
    public ApiResponse<PageResponse<AdminOrderDto>> findAllForOrderAdmin(String search, Pageable pageable) {
        Page<Order> orders = orderRepository.findOrderAdmin(search, pageable);

        List<AdminOrderDto> content = orders.getContent().stream()
                .map(order -> {

                    //Lay DTO cua customer
                    DisplayAdminCustomerResponse customer =
                            customerService.getOrderCustomer(order.getCustomerId());

                    //Lay DTO cua address
                    DisplayAdminAddressResponse address =
                            addressService.getOrderAddress(order.getCustomerId());

                    AdminOrderDto dto = adminOrderMapper.toAdminOrderDto(order, customer, address);

                    // Fill productName cho tung item
                    if (dto.getItems() == null || dto.getItems().isEmpty()) {
                        return dto;
                    }

                    // 1) Gom tat ca productId cua order nay
                    Set<UUID> productIds = dto.getItems().stream()
                            .map(OrderItemDto::getProductId)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toSet());

                    // 2) Query 1 lần lấy product
                    Map<UUID, String> productNameById = productRepository.findAllById(productIds).stream()
                            .collect(Collectors.toMap(Product::getId, Product::getName));

                    // 3) Gán productName cho từng item
                    dto.getItems().forEach(item ->
                            item.setProductName(productNameById.get(item.getProductId()))
                    );

                    return dto;

                })
                .toList();

        PageResponse<AdminOrderDto> pageResponse = PageResponse.<AdminOrderDto>builder()
                .content(content)
                .pageNumber(orders.getNumber())
                .pageSize(orders.getSize())
                .totalPages(orders.getTotalPages())
                .totalElements(orders.getTotalElements())
                .last(orders.isLast())
                .build();

        return ApiResponse.<PageResponse<AdminOrderDto>>builder()
                .success(true)
                .message("Orders retrieved successfully!")
                .data(pageResponse)
                .build();
    }
}
