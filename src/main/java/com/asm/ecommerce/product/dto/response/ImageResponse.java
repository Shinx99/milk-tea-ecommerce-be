package com.asm.ecommerce.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageResponse {
    private UUID id;
    private String secureUrl;
    private String publicId;
    private String altText;
    private Boolean isPrimary; // ✅ GHI CHÚ: Dùng trường này để Frontend lọc/sắp xếp ảnh chính
}