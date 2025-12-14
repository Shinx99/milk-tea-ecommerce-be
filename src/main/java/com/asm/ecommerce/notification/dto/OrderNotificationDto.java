package com.asm.ecommerce.notification.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderNotificationDto implements Serializable {

    public enum NotificationType {
        ORDER_PAID,
        ORDER_COMPlETE
    }

    private UUID id;
    private String orderCode;       // mã đơn hiển thị cho quán
    private BigDecimal total;
    private String status;          // PAID, COMPLETE,...
    private Instant placedAt;
    private NotificationType type;

}
