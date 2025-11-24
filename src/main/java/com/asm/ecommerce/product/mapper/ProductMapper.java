package com.asm.ecommerce.product.mapper;

import com.asm.ecommerce.product.domain.Image;
import com.asm.ecommerce.product.domain.Product;
import com.asm.ecommerce.product.domain.ProductCategory;
import com.asm.ecommerce.product.dto.request.ProductRequest;
import com.asm.ecommerce.product.dto.response.CategoryResponse;
import com.asm.ecommerce.product.dto.response.ProductResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProductMapper {
    private ProductMapper() {}

    public static ProductResponse toResponse(Product product) {
        if (product == null) return null;

        ProductCategory cat = product.getCategory();
        CategoryResponse catResponse = null;
        UUID catId = null;

        if (cat != null) {
            catId = cat.getId();
            // GỌI HÀM MAP ĐỆ QUY MỚI Ở ĐÂY
            catResponse = toCategoryResponseRecursive(cat);
        }

        List<String> images = (product.getImages() != null)
                ? product.getImages().stream().map(Image::getSecureUrl).collect(Collectors.toList())
                : Collections.emptyList();

        return ProductResponse.builder()
                .id(product.getId())
                .categoryId(catId)
                .category(catResponse) // Object này giờ đã chứa full cây con (Size, Đá...)
                .name(product.getName())
                .quantity(product.getQuantity())
                .active(product.getActive())
                .description(product.getDescription())
                .price(product.getPrice())
                .imageUrl(images)
                .build();
    }

    // --- HÀM MỚI QUAN TRỌNG: MAP ĐỆ QUY CATEGORY ---
    // Hàm này sẽ tự động chui xuống lấy con (Size, Đá) và cháu (S, M, L...)
    private static CategoryResponse toCategoryResponseRecursive(ProductCategory cat) {

        // [DÒNG 1] Kiểm tra an toàn: Nếu đưa vào null thì trả về null, khỏi làm gì cả.
        if (cat == null) return null;

        // [DÒNG 2] "Xây nhà": Tạo ra cái vỏ Object DTO (CategoryResponse) trước.
        // Lúc này mới chỉ map thông tin cơ bản của "Trà Sữa" (ID, Tên, Cha là ai).
        CategoryResponse.CategoryResponseBuilder builder = CategoryResponse.builder()
                .id(cat.getId())
                .categoryName(cat.getCategoryName())
                .isActive(cat.getActive())
                .sortOrder(cat.getSortOrder())
                .parentId(cat.getParent() != null ? cat.getParent().getId() : null);

        // [DÒNG 3] Kiểm tra xem "Trà Sữa" có con không?
        // Trà Sữa có con là [Size, Đá] -> Điều kiện này TRUE.
        if (cat.getChildren() != null && !cat.getChildren().isEmpty()) {

            // [DÒNG 4 - QUAN TRỌNG NHẤT] Stream & Map đệ quy
            // Đoạn này dịch ra tiếng Việt là: "Với mỗi đứa con của Trà Sữa, hãy chạy lại cái quy trình này từ đầu".
            List<CategoryResponse> childDtos = cat.getChildren().stream()
                    .map(ProductMapper::toCategoryResponseRecursive) // Gọi lại chính nó
                    .collect(Collectors.toList());

            builder.children(childDtos);
        }

        return builder.build();
    }

    public static List<ProductResponse> toResponseList(List<Product> products) {
        if (products == null || products.isEmpty()) {
            return List.of();
        }
        // Dùng stream cho ngắn gọn và hiện đại
        return products.stream()
                .map(ProductMapper::toResponse)
                .collect(Collectors.toList());
    }

    public static Product CreateEntity(ProductRequest req) {
        if(req == null) return null;
        return Product.builder()
                .name(req.getName())
                .description(req.getDescription())
                .quantity(req.getQuantity())
                .price(req.getPrice())
                .active(req.getActive())
                .build();
    }

    public static void UpdateEntity (Product entity, ProductRequest req) {
        if(entity == null || req == null) return;

        if(req.getName() != null) entity.setName(req.getName());
        if(req.getDescription() != null) entity.setDescription(req.getDescription());
        if(req.getQuantity() != null) entity.setQuantity(req.getQuantity());
        if(req.getPrice() != null) entity.setPrice(req.getPrice());
        if(req.getActive() != null) entity.setActive(req.getActive());
    }
}