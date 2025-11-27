package com.asm.ecommerce.cart.mapper;

import com.asm.ecommerce.cart.domain.Cart;
import com.asm.ecommerce.cart.dto.request.AddToCartRequestDto;
import com.asm.ecommerce.cart.dto.response.CartItemResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    /**
     * Map từ AddToCartRequestDto sang Cart entity
     * Dùng cho thao tác thêm mới sản phẩm vào giỏ
     */
    @Mapping(target = "id", expression = "java(UUID.randomUUID())")
    @Mapping(target = "customerId", source = "customerId")
    @Mapping(target = "productId", source = "dto.productId")
    @Mapping(target = "quantity", source = "dto.quantity")
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "price", ignore = true) // Sẽ set trong service từ Product
    @Mapping(target = "createdAt", ignore = true) // Auto-generated
    @Mapping(target = "updatedAt", ignore = true) // Auto-generated
    @Mapping(target = "expiresAt", ignore = true) // Set trong service nếu cần
    @Mapping(target = "sizeCategoryId", source = "dto.sizeCategoryId")
    @Mapping(target = "sugarCategoryId", source = "dto.sugarCategoryId")
    @Mapping(target = "iceCategoryId", source = "dto.iceCategoryId")
    @Mapping(target = "temperatureCategoryId", source = "dto.temperatureCategoryId")
    Cart toEntity(AddToCartRequestDto dto, UUID customerId);

    /**
     * Map từ Cart entity sang CartItemResponseDto
     * Dùng cho API trả về thông tin item trong giỏ
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "unitPrice", source = "price")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "addedAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "sizeCategoryId", source = "sizeCategoryId")
    @Mapping(target = "sugarCategoryId", source = "sugarCategoryId")
    @Mapping(target = "iceCategoryId", source = "iceCategoryId")
    @Mapping(target = "temperatureCategoryId", source = "temperatureCategoryId")
    @Mapping(target = "sizeName", ignore = true)
    @Mapping(target = "sugarName", ignore = true)
    @Mapping(target = "iceName", ignore = true)
    @Mapping(target = "temperatureName", ignore = true)
    @Mapping(target = "productName", ignore = true) // Sẽ set từ ProductService
    @Mapping(target = "productImage", ignore = true) // Sẽ set từ ProductService
    @Mapping(target = "totalPrice", ignore = true) // Tính toán tự động trong DTO
    CartItemResponseDto toResponseDto(Cart cart);

    /**
     * Map list Cart entities sang list CartItemResponseDto
     * Dùng cho API trả về nhiều items
     */
    List<CartItemResponseDto> toResponseDtoList(List<Cart> carts);
}
