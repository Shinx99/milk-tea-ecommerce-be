package com.asm.ecommerce.order.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateOrderRequestDto {

    private UUID customerId;
    private String customerName;
    private String phone;
    private String address;
    private String note;
    private List<OrderItemRequestDto> items;

}
