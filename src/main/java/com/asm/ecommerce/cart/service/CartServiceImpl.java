package com.asm.ecommerce.cart.service;

import com.asm.ecommerce.cart.domain.Cart;
import com.asm.ecommerce.cart.dto.product.ProductInfoDto;
import com.asm.ecommerce.cart.dto.request.AddToCartRequestDto;
import com.asm.ecommerce.cart.dto.request.RemoveCartRequestDto;
import com.asm.ecommerce.cart.dto.request.UpdateCartRequestDto;
import com.asm.ecommerce.cart.dto.response.CartItemResponseDto;
import com.asm.ecommerce.cart.dto.response.CartSummaryResponseDto;
import com.asm.ecommerce.cart.mapper.CartItemMapper;
import com.asm.ecommerce.cart.mapper.ProductInfoMapper;
import com.asm.ecommerce.cart.repository.CartRepository;
import com.asm.ecommerce.customer.service.customer.CustomerService;
import com.asm.ecommerce.product.dto.response.ProductResponse;
import com.asm.ecommerce.product.service.CategoryService;
import com.asm.ecommerce.product.service.ProductService;
import com.asm.ecommerce.shared.exception.BadRequestException;
import com.asm.ecommerce.shared.exception.BusinessException;
import com.asm.ecommerce.shared.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;
    private final CartItemMapper cartItemMapper;
    private final ProductService productService;

    private final ProductInfoMapper productInfoMapper;
    private final CustomerService customerService;

    private final CategoryService categoryService;


    @Override
    @Transactional
    public CartItemResponseDto addToCart(UUID userId, AddToCartRequestDto requestDto) {

        UUID customerId = customerService.getCustomerIdByUserId(userId);

        log.info("Adding product {} to cart for customer {}",
                requestDto.getProductId(), customerId);

        // 1. Validate quantity
        if (requestDto.getQuantity() <= 0) {
            throw new BadRequestException("Quantity must be greater than 0");
        }

        // 2. Gọi ProductService qua interface, nhận ProductResponse DTO
        ProductResponse productResponse = productService.getProductInfoForCart(requestDto.getProductId());

        // 3. Convert ProductResponse sang ProductInfoDto bằng mapper
        ProductInfoDto product = productInfoMapper.fromProductResponse(productResponse);

        // 4. Validate product
        validateProductForCart(product, requestDto.getQuantity());

        // 5. Check sản phẩm đã có trong giỏ chưa
        Cart existingCart = cartRepository.findByCustomerIdAndProductIdAndSizeCategoryIdAndSugarCategoryIdAndIceCategoryIdAndTemperatureCategoryIdAndStatus(
                customerId,
                requestDto.getProductId(),
                requestDto.getSizeCategoryId(),
                requestDto.getSugarCategoryId(),
                requestDto.getIceCategoryId(),
                requestDto.getTemperatureCategoryId(),
                "active"
        ).orElse(null);


        Cart cart;
        if (existingCart != null) {
            // Tăng quantity
            int newQuantity = existingCart.getQuantity() + requestDto.getQuantity();

            if (!product.canAddToCart(newQuantity)) {
                throw new BusinessException(
                        String.format("Số lượng vượt quá tồn kho. Chỉ còn %d sản phẩm",
                                product.getQuantity())
                );
            }

            existingCart.setQuantity(newQuantity);
            cart = cartRepository.save(existingCart);
            log.info("Updated cart item quantity to {}", newQuantity);
        } else {
            // Tạo mới
            cart = cartItemMapper.toEntity(requestDto, customerId);
            cart.setPrice(product.getPrice());
            cart.setExpiresAt(Instant.now().plus(7, ChronoUnit.DAYS));
            cart.setStatus("active");
            cart = cartRepository.save(cart);
            log.info("Created new cart item with id: {}", cart.getId());
        }

        // 6. Map sang response và enrichment
        CartItemResponseDto responseDto = cartItemMapper.toResponseDto(cart);
        enrichOptionNames(responseDto);
        enrichProductInfo(responseDto, product);

        return responseDto;
    }

    @Override
    @Transactional
    public CartSummaryResponseDto getCartByUserId(UUID userId) {
        //Lookup customerId từ userId - nghiệp vụ cross-feature thực hiện ở service
        UUID customerId = customerService.getCustomerIdByUserId(userId);

        log.info("Getting cart for customer: {}", customerId);

        List<Cart> allItems = cartRepository.findByCustomerId(customerId);
        log.info("Found {} cart items for customerId: {}", allItems.size(), customerId);


        List<CartItemResponseDto> activeItems = allItems.stream()
                .filter(cart -> "active".equals(cart.getStatus()))
                .map(cart -> {
                    CartItemResponseDto dto = cartItemMapper.toResponseDto(cart);
                    try {
                        enrichOptionNames(dto);
                        enrichProductInfo(dto, cart.getProductId());
                    } catch (Exception e) {
                        log.error("Error enriching product {} for cart item {}",
                                cart.getProductId(), cart.getId(), e);
                        dto.setProductName("[Lỗi tải sản phẩm]");
                        dto.setProductImage(getDefaultImageUrl());
                    }
                    return dto;
                })
                .collect(Collectors.toList());

        List<CartItemResponseDto> savedItems = allItems.stream()
                .filter(cart -> "abandoned".equals(cart.getStatus()))
                .map(cart -> {
                    CartItemResponseDto dto = cartItemMapper.toResponseDto(cart);
                    enrichOptionNames(dto);
                    enrichProductInfo(dto, cart.getProductId());
                    return dto;
                })
                .collect(Collectors.toList());

        BigDecimal subTotal = activeItems.stream()
                .map(CartItemResponseDto::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //Không tính thuế
        BigDecimal tax = BigDecimal.ZERO;
        //BigDecimal tax = subTotal.multiply(BigDecimal.valueOf(0.1));

        // TODO: sau này tích hợp Grab/đối tác ship thì thay đổi logic sau
        BigDecimal shippingFee = BigDecimal.ZERO;
    /*
        BigDecimal shippingFee = subTotal.compareTo(BigDecimal.valueOf(500000)) > 0
                ? BigDecimal.ZERO
                : BigDecimal.valueOf(30000);
    */

        BigDecimal total = subTotal.add(tax).add(shippingFee);

        return new CartSummaryResponseDto(
                activeItems, savedItems,
                activeItems.size(), savedItems.size(),
                subTotal, tax, shippingFee, total, "VND"
        );
    }

    @Override
    @Transactional
    public CartItemResponseDto updateCartItem(UUID cartItemId, UpdateCartRequestDto request) {
        log.info("Updating cart item {} with quantity {}", cartItemId, request.getQuantity());

        Cart cart = cartRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found with id: " + cartItemId));

        /*cart.setSizeCategoryId(request.getSizeCategoryId());
        cart.setSugarCategoryId(request.getSugarCategoryId());
        cart.setIceCategoryId(request.getIceCategoryId());
        cart.setTemperatureCategoryId(request.getTemperatureCategoryId());*/

        // Validate quantity
        if (request.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        cart.setQuantity(request.getQuantity());
        Cart updated = cartRepository.save(cart);

        CartItemResponseDto response = cartItemMapper.toResponseDto(updated);
        /*response.setSizeName(categoryService.getCategoryNameById(updated.getSizeCategoryId()));  //Test gán name thủ công
        response.setSugarName(categoryService.getCategoryNameById(updated.getSugarCategoryId()));
        response.setIceName(categoryService.getCategoryNameById(updated.getIceCategoryId()));
        response.setTemperatureName(categoryService.getCategoryNameById(updated.getTemperatureCategoryId()));*/
        enrichOptionNames(response);
        enrichProductInfo(response, updated.getProductId());

        return response;
    }

    @Override
    @Transactional
    public void removeCartItems(RemoveCartRequestDto request) {
        log.info("Removing {} cart items", request.getCartItemIds().size());

        request.getCartItemIds().forEach(cartItemId -> {
            Cart cart = cartRepository.findById(cartItemId)
                    .orElseThrow(() -> new RuntimeException("Cart item not found with id: " + cartItemId));

            cart.setStatus("abandoned");
            cartRepository.save(cart);
        });

        log.info("Successfully removed cart items");
    }

    @Override
    @Transactional
    public void clearCart(UUID customerId) {
        log.info("Clearing cart for customer: {}", customerId);

        List<Cart> carts = cartRepository.findByCustomerIdAndStatus(
                customerId,
                "active"
        );

        carts.forEach(cart -> {
            cart.setStatus("abandoned");
            cartRepository.save(cart);
        });

        log.info("Cleared {} items from cart", carts.size());
    }

    @Override
    @Transactional
    public CartItemResponseDto saveForLater(UUID cartItemId) {
        log.info("Saving cart item {} for later", cartItemId);

        Cart cart = cartRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found with id: " + cartItemId));

        cart.setStatus("abandoned");
        Cart updated = cartRepository.save(cart);

        CartItemResponseDto response = cartItemMapper.toResponseDto(updated);
        enrichOptionNames(response);
        enrichProductInfo(response, updated.getProductId());

        return response;
    }

    @Override
    @Transactional
    public CartItemResponseDto moveToActive(UUID cartItemId) {
        log.info("Moving cart item {} to active", cartItemId);

        Cart cart = cartRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found with id: " + cartItemId));

        cart.setStatus("active");
        Cart updated = cartRepository.save(cart);

        CartItemResponseDto response = cartItemMapper.toResponseDto(updated);
        enrichOptionNames(response);
        enrichProductInfo(response, updated.getProductId());

        return response;
    }

    /**
     * Enrichment thông tin product từ ProductService
     * TODO: Implement khi có ProductService
     */
    /**
     * Enrichment với ProductInfoDto đã có
     */
    private void enrichProductInfo(CartItemResponseDto dto, ProductInfoDto product) {
        String name = product.getName() != null ? product.getName() : "[Không có tên]";
        if (product.getIsLowStock()) {
            name += " (Sắp hết)";
        }
        if (product.getIsOutOfStock()) {
            name += " (Hết hàng)";
        }
        dto.setProductName(name);

        dto.setProductImage(product.getImageUrl() != null
                ? product.getImageUrl()
                : getDefaultImageUrl());
        dto.setUnitPrice(product.getPrice());
    }




    /**
     * Overload: Fetch product từ service rồi enrichment
     */
    private void enrichProductInfo(CartItemResponseDto dto, UUID productId) {
        try {
            log.info("Enriching product for productId: {}", productId);

            ProductResponse productResponse = productService.getProductInfoForCart(productId);
            log.info("Got ProductResponse: {}", productResponse.getName());

            ProductInfoDto product = productInfoMapper.fromProductResponse(productResponse);
            log.info("Mapped ProductInfoDto: {}", product);

            if (product == null) {
                log.warn("Product mapping returned null");
                dto.setProductName("[Lỗi mapping]");
                dto.setProductImage(getDefaultImageUrl());
                return;
            }

            enrichProductInfo(dto, product);
            log.info("Enrichment success: {}", dto.getProductName());

        } catch (ResourceNotFoundException e) {
            log.warn("Product not found for enrichment: {}", productId);
            dto.setProductName("[Sản phẩm không tồn tại]");
            dto.setProductImage(getDefaultImageUrl());
        } catch (Exception e) {
            log.error("Error enriching product {}", productId, e);
            dto.setProductName("[Lỗi tải sản phẩm]");
            dto.setProductImage(getDefaultImageUrl());
        }
    }


    private String getDefaultImageUrl() {
        return "https://via.placeholder.com/300x300?text=No+Image";
    }

    /**
     * Validate sản phẩm có thể thêm vào giỏ
     */
    private void validateProductForCart(ProductInfoDto product, Integer requestedQuantity) {
        // null hoặc false đều xem như không active
        if (!Boolean.TRUE.equals(product.getIsActive())) {
            throw new BusinessException("Sản phẩm này hiện không còn bán");
        }

        if (product.getIsOutOfStock()) { // method này đã null-safe
            throw new BusinessException("Sản phẩm đã hết hàng");
        }

        if (!product.canAddToCart(requestedQuantity)) {
            throw new BusinessException(
                    String.format("Số lượng không đủ. Chỉ còn %d sản phẩm", product.getQuantity())
            );
        }
    }

    /**
     * Enrichment với CartItemResponseDto đã có
     */
    private void enrichOptionNames(CartItemResponseDto dto) {
        if (dto.getSizeCategoryId() != null) {
            dto.setSizeName(categoryService.getCategoryNameById(dto.getSizeCategoryId()));
        }
        if (dto.getSugarCategoryId() != null) {
            dto.setSugarName(categoryService.getCategoryNameById(dto.getSugarCategoryId()));
        }
        if (dto.getIceCategoryId() != null) {
            dto.setIceName(categoryService.getCategoryNameById(dto.getIceCategoryId()));
        }
        if (dto.getTemperatureCategoryId() != null) {
            dto.setTemperatureName(categoryService.getCategoryNameById(dto.getTemperatureCategoryId()));
        }
    }



    /*
            Cart Feature                  Product Feature
            ↓                              ↓
        CartService  --calls-->  ProductService (interface)
            ↓                              ↓
        receives ProductResponse DTO ←---returns
            ↓
        Convert to ProductInfoDto (via mapper)
            ↓
        Use in business logic
    */

}
