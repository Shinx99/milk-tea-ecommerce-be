package com.asm.ecommerce.cart.mapper;

import com.asm.ecommerce.cart.dto.product.ProductInfoDto;
import com.asm.ecommerce.product.dto.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

// 1. ProductInfoMapper trong cart/mapper
@Mapper(componentModel = "spring")
public interface ProductInfoMapper {
    @Mapping(target = "imageUrl", source = "imageUrl", qualifiedByName = "getPrimaryImage")
    @Mapping(target = "isActive", source = "active")
    ProductInfoDto fromProductResponse(ProductResponse productResponse);

    @Named("getPrimaryImage")
    default String getPrimaryImage(List<String> imageUrls) {
        return (imageUrls != null && !imageUrls.isEmpty()) ? imageUrls.get(0) : null;
    }
}

