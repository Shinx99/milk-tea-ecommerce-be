package com.asm.ecommerce.product.service;

import com.asm.ecommerce.product.domain.Image;
import com.asm.ecommerce.product.domain.Product;
import com.asm.ecommerce.product.domain.ProductCategory;
import com.asm.ecommerce.product.dto.request.ProductRequest;
import com.asm.ecommerce.product.dto.response.ProductResponse;
import com.asm.ecommerce.product.mapper.ProductMapper;
import com.asm.ecommerce.product.repository.ProductCategoryRepository;
import com.asm.ecommerce.product.repository.ProductRepository;
import com.asm.ecommerce.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repo;
    private final ProductCategoryRepository categoryRepo;

    // --- Phương thức READ/DELETE giữ nguyên ---

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> searchProductsByName(String name) {
        return ProductMapper.toResponseList(repo.findByNameContainingIgnoreCase(name));    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> findAll() {
        return ProductMapper.toResponseList(repo.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> findByProductCategoryName(String categoryName) {
        return ProductMapper.toResponseList(repo.findByCategory_CategoryName(categoryName));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> findByProductCategoryId(UUID categoryId) {
        return ProductMapper.toResponseList(repo.findByCategoryId(categoryId));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse findById(UUID id) {
        Product p = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id not found"));
        return ProductMapper.toResponse(p);
    }

    // --- PHƯƠNG THỨC CREATE (Đã đồng bộ imageUrl và xử lý publicId) ---

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
    public void delete(UUID id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Product not found ");
        }
        repo.deleteById(id);
    }
}
