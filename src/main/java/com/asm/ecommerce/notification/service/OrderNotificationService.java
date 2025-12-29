package com.asm.ecommerce.notification.service;

import com.asm.ecommerce.notification.dto.OrderNotificationDto;
import com.asm.ecommerce.notification.mapper.OrderNotificationMapper;
import com.asm.ecommerce.order.dto.response.AdminOrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderNotificationService {

    private final OrderNotificationMapper mapper;
    private final SimpMessagingTemplate messagingTemplate;

    public void sendOrderPaidNotification(AdminOrderDto adminOrderDto){

        // 1. Map sang DTO cho WebSocket
        OrderNotificationDto dto = mapper.toDto(adminOrderDto);

        // 2. Gui ra topic cho admin/kitchen dang subcribe
        messagingTemplate.convertAndSend("/topic/orders", dto);
    }

}
