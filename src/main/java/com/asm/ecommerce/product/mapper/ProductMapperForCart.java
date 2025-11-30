package com.asm.ecommerce.product.mapper;

import com.asm.ecommerce.product.domain.Image;
import com.asm.ecommerce.product.domain.Product;
import com.asm.ecommerce.product.dto.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductMapperForCart {

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "imageUrl",  source = "images", qualifiedByName = "mapImages")
    @Mapping(target = "active",    source = "active")
    ProductResponse toDto(Product product);


    /**
     * Map List<Image> sang List<String> imageUrls
     * Sắp xếp: isPrimary = true lên đầu, sau đó theo thứ tự
     */
    @Named("mapImages")
    default List<String> mapImages(List<Image> images) {
        if (images == null || images.isEmpty()) {
            return Collections.emptyList();
        }

        return images.stream()
                .sorted(Comparator.comparing(Image::getIsPrimary,
                        Comparator.reverseOrder())) // isPrimary=true lên đầu
                .map(Image::getSecureUrl)
                .collect(Collectors.toList());
    }

}
