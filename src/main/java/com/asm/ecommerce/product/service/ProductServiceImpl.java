package com.asm.ecommerce.product.service;

import com.asm.ecommerce.product.domain.Image;
import com.asm.ecommerce.product.domain.Product;
import com.asm.ecommerce.product.domain.ProductCategory;
import com.asm.ecommerce.product.dto.request.ProductRequest;
import com.asm.ecommerce.product.dto.response.ProductResponse;
import com.asm.ecommerce.product.mapper.ProductMapper;
import com.asm.ecommerce.product.mapper.ProductMapperForCart;
import com.asm.ecommerce.product.repository.ProductCategoryRepository;
import com.asm.ecommerce.product.repository.ProductRepository;
import com.asm.ecommerce.shared.dto.ApiResponse;
import com.asm.ecommerce.shared.dto.PageResponse;
import com.asm.ecommerce.shared.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repo;
    private final ProductCategoryRepository categoryRepo;

    private final ProductMapperForCart productMapperForCart;


    // Method: Hien thi cho ca trang Product
    //Display for Customer
    @Transactional(readOnly = true)
    @Override
    public ApiResponse<PageResponse<ProductResponse>> findAllByActiveTrueAndFilter(String keyword, String categoryName, Pageable pageable) {
        Page<Product> products = repo.searchProducts(keyword, categoryName, pageable);

        PageResponse<ProductResponse> pageResponse = PageResponse.<ProductResponse>builder()
                .content(products.getContent().stream()
                        .map(ProductMapper::toResponse)
                        .toList())
                .pageNumber(products.getNumber())
                .pageSize(products.getSize())
                .totalPages(products.getTotalPages())
                .totalElements(products.getTotalElements())
                .last(products.isLast())
                .build();

        return ApiResponse.<PageResponse<ProductResponse>>builder()
                .success(true)
                .message("Products retrieved successfully!")
                .data(pageResponse)
                .build();
    }



    @Transactional(readOnly = true)
    @Override
    public ApiResponse<PageResponse<ProductResponse>> findByProductCategoryId(UUID categoryId, Pageable pageable) {
        Page<Product> products = repo.findByCategoryId(categoryId, pageable);

        PageResponse<ProductResponse> pageResponse = PageResponse.<ProductResponse>builder()
                .content(products.getContent().stream()
                        .map(ProductMapper::toResponse)
                        .toList())
                .pageNumber(products.getNumber())
                .pageSize(products.getSize())
                .totalPages(products.getTotalPages())
                .totalElements(products.getTotalElements())
                .last(products.isLast())
                .build();

        return ApiResponse.<PageResponse<ProductResponse>>builder()
                .success(true)
                .message("Products retrieved successfully!")
                .data(pageResponse)
                .build();
    }

    // Cho Product Details
    @Override
    @Transactional(readOnly = true) // Giữ Transaction để Mapper load Lazy
    public ApiResponse<ProductResponse> findById(UUID id) {

        // BƯỚC 1: Tìm sản phẩm (Entity Graph đã fetch sẵn Category & Images rồi)
        Product p = repo.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id not found"));

        // BƯỚC 2: Chỉ cần gọi Mapper thần thánh
        // Mapper sẽ tự động chui vào rootCat -> children -> children... để map hết
        ProductResponse dto = ProductMapper.toResponse(p);

        // BƯỚC 3: Trả về
        return ApiResponse.<ProductResponse>builder()
                .success(true)
                .message("Product retrieved successfully!")
                .data(dto)
                .build();
    }


    // Display for ADMIN
    @Transactional(readOnly = true)
    @Override
    public ApiResponse<PageResponse<ProductResponse>> findAll(Pageable pageable) {
        Page<Product> products = repo.findAll(pageable);

        PageResponse<ProductResponse> pageResponse = PageResponse.<ProductResponse>builder()
                .content(products.getContent().stream()
                        .map(ProductMapper::toResponse)
                        .toList())
                .pageNumber(products.getNumber())
                .pageSize(products.getSize())
                .totalPages(products.getTotalPages())
                .totalElements(products.getTotalElements())
                .last(products.isLast())
                .build();

        return ApiResponse.<PageResponse<ProductResponse>>builder()
                .success(true)
                .message("Products retrieved successfully!")
                .data(pageResponse)
                .build();
    }


    //Cho method relatedProduct
    @Transactional(readOnly = true)
    @Override
    public ApiResponse<PageResponse<ProductResponse>> findByCategoryIdAndIdNot(UUID id, Pageable pageable){

        Product current = repo.findByIdAndActiveTrue(id).orElseThrow(() -> new ResourceNotFoundException("Id not found"));
        UUID categoryId = current.getCategory().getId();

        Page<Product> products = repo.findByCategory_IdAndIdNot(categoryId, id, pageable);

        PageResponse<ProductResponse> pageResponse = PageResponse.<ProductResponse>builder()
                .content(products.getContent().stream()
                        .map(ProductMapper::toResponse)
                        .toList())
                .pageNumber(products.getNumber())
                .pageSize(products.getSize())
                .totalPages(products.getTotalPages())
                .totalElements(products.getTotalElements())
                .last(products.isLast())
                .build();

        return ApiResponse.<PageResponse<ProductResponse>>builder()
                .success(true)
                .message("Products retrieved successfully!")
                .data(pageResponse)
                .build();
    }

    // for admin---------------------------------------------------------------------------------------------------------------
    // --- PHƯƠNG THỨC CREATE (Đã đồng bộ imageUrl và xử lý publicId) ---

    // Method: Hien thi cho ca trang Product
    //Display for Customer
    @Transactional(readOnly = true)
    @Override
    public ApiResponse<PageResponse<ProductResponse>> findAllByForAdmin(String keyword, String categoryName, Pageable pageable) {
        Page<Product> products = repo.searchProductsForAdmin(keyword, categoryName, pageable);

        PageResponse<ProductResponse> pageResponse = PageResponse.<ProductResponse>builder()
                .content(products.getContent().stream()
                        .map(ProductMapper::toResponse)
                        .toList())
                .pageNumber(products.getNumber())
                .pageSize(products.getSize())
                .totalPages(products.getTotalPages())
                .totalElements(products.getTotalElements())
                .last(products.isLast())
                .build();

        return ApiResponse.<PageResponse<ProductResponse>>builder()
                .success(true)
                .message("Products retrieved successfully!")
                .data(pageResponse)
                .build();
    }

    @Override
    @Transactional
    public ProductResponse create(ProductRequest req) {

        Product entity = ProductMapper.CreateEntity(req);

        ProductCategory category = categoryRepo.findById(req.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category Id not found: " + req.getCategoryId()));
        entity.setCategory(category);

        // Xử lý Images từ URL (Dùng req.getImageUrl())
        if (req.getImageUrl() != null && !req.getImageUrl().isEmpty()) {
            List<Image> images = req.getImageUrl().stream()
                    .map(url -> Image.builder()
                            .secureUrl(url)
                            .product(entity)
                            // Gán giá trị mặc định cho publicId để tránh lỗi @NotNull
                            .publicId(UUID.randomUUID().toString())
                            .isPrimary(req.getImageUrl().indexOf(url) == 0)
                            .build())
                    .collect(Collectors.toList());

            entity.setImages(images);
        }

        Product savedProduct = repo.save(entity);
        return ProductMapper.toResponse(savedProduct);
    }


    // --- PHƯƠNG THỨC UPDATE (Đã đồng bộ imageUrl và xử lý publicId) ---

    @Override
    @Transactional
    public ProductResponse update(UUID id, ProductRequest req) {
        Product entity = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id not found: " + id));

        // 1. Cập nhật các trường cơ bản
        ProductMapper.UpdateEntity(entity, req);

        // 2. Cập nhật Category
        if (!entity.getCategory().getId().equals(req.getCategoryId())) {
            ProductCategory category = categoryRepo.findById(req.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category Id not found: " + req.getCategoryId()));
            entity.setCategory(category);
        }

        // 3. Xử lý Cập nhật Images

        // BƯỚC 1: Xóa ảnh cũ (Kích hoạt DELETE do orphanRemoval=true)
        if (entity.getImages() != null) {
            entity.getImages().clear();
            repo.saveAndFlush(entity);
        }

        // BƯỚC 2: Tạo và thêm ảnh mới (Dùng req.getImageUrl())
        if (req.getImageUrl() != null && !req.getImageUrl().isEmpty()) {
            List<Image> newImages = req.getImageUrl().stream()
                    .map(url -> Image.builder()
                            .secureUrl(url)
                            .product(entity)
                            // Gán giá trị mặc định cho publicId
                            .publicId(UUID.randomUUID().toString())
                            .isPrimary(req.getImageUrl().indexOf(url) == 0)
                            .build())
                    .collect(Collectors.toList());

            // Thêm tất cả ảnh mới vào danh sách hiện tại của Entity
            if (entity.getImages() == null) {
                entity.setImages(newImages);
            } else {
                entity.getImages().addAll(newImages);
            }
        }

        // 4. Lưu và trả về
        Product saved = repo.save(entity);
        return ProductMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        int updated = repo.softDeleteById(id, OffsetDateTime.now());
        if(updated == 0){
            throw new EntityNotFoundException("Product not found or already inactive");
        }
    }

    @Override
    public List<ProductResponse> searchProductsByName(String name) {
        return List.of();
    }



    //todo: ============= Cart ===================----------------------------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductInfoForCart(UUID productId) {
        log.debug("Getting product by id: {}", productId);

        //  Dùng query fetch images luôn
        Product product = repo.findByIdWithImages(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with id: " + productId
                ));

        return productMapperForCart.toDto(product);
    }
}
