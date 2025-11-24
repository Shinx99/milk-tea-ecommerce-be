package com.asm.ecommerce.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptionGroupDTO {
    private UUID id;          // ID của nhóm (ví dụ: ID của nhóm Size)
    private String name;      // Tên nhóm: "Size", "Mức đá"
    private List<OptionValueDTO> values; // Danh sách các lựa chọn bên trong
}
