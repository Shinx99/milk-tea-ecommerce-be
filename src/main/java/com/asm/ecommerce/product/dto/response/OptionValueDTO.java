package com.asm.ecommerce.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptionValueDTO {
    private UUID id;          // ID của option (ví dụ: ID của Size L)
    private String name;      // Tên hiển thị: "L", "Ít đá"
    private BigDecimal price; // Giá cộng thêm (nếu có, ví dụ Size L +5k)
}
