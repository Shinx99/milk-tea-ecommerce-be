package com.asm.ecommerce.cart.controller;

import com.asm.ecommerce.cart.dto.request.AddToCartRequestDto;
import com.asm.ecommerce.cart.dto.request.RemoveCartRequestDto;
import com.asm.ecommerce.cart.dto.request.UpdateCartRequestDto;
import com.asm.ecommerce.cart.dto.response.CartItemResponseDto;
import com.asm.ecommerce.cart.dto.response.CartSummaryResponseDto;
import com.asm.ecommerce.cart.service.CartService;
import com.asm.ecommerce.shared.dto.ApiResponse;
import com.asm.ecommerce.shared.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final CartService cartService;

    //todo: add cartItem
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CartItemResponseDto>> addToCart(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody @Valid AddToCartRequestDto request
    ) {
        UUID userId = userPrincipal.getId();
        log.info("Adding item to cart for user: {}", userId);
        CartItemResponseDto data = cartService.addToCart(userId, request);

        ApiResponse<CartItemResponseDto> response = ApiResponse.<CartItemResponseDto>builder()
                .success(true)
                .message("Thêm vào giỏ hàng thành công")
                .data(data)
                .timeStamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }


    //todo: take the cart
    @GetMapping
    public ResponseEntity<ApiResponse<CartSummaryResponseDto>> getCart(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ){
        UUID userId = userPrincipal.getId();
        log.info("Getting cart for userId: {}", userId);

        CartSummaryResponseDto data = cartService.getCartByUserId(userId);

        ApiResponse<CartSummaryResponseDto> response = ApiResponse.<CartSummaryResponseDto>builder()
                .success(true)
                .message("Lấy giỏ hàng thành công")
                .data(data)
                .timeStamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    //todo: update quantity
    @PutMapping("/{cartItemId}")
    public ResponseEntity<ApiResponse<CartItemResponseDto>> updateCartItem(
            @PathVariable UUID cartItemId,
            @RequestBody @Valid UpdateCartRequestDto request
    ){

        log.info("Updating cart item: {}", cartItemId);

        CartItemResponseDto data = cartService.updateCartItem(cartItemId, request);
        ApiResponse<CartItemResponseDto> response = ApiResponse.<CartItemResponseDto>builder()
                .success(true)
                .message("Cập nhật giỏ hàng thành công")
                .data(data)
                .timeStamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    //todo: delete cartItem
    @DeleteMapping("/remove")
    public ResponseEntity<ApiResponse<Void>> removeCart(
            @RequestBody @Valid RemoveCartRequestDto request
    ){

        log.info("Removing items from cart");
        cartService.removeCartItems(request);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Xóa sản phẩm thành công")
                .timeStamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);

    }


    //todo: clear cart
    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse<Void>> clearCart(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ){
        UUID userId = userPrincipal.getId();
        log.info("Clearing cart for customer: {}", userId);
        cartService.clearCart(userId);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Xóa giỏ hàng thành công")
                .timeStamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    //todo: save for later
    @PutMapping("/{cartItemId}/save-for-later")
    public ResponseEntity<ApiResponse<CartItemResponseDto>> saveForLater(
            @PathVariable UUID cartItemId
    ){
        log.info("Saving item for later: {}", cartItemId);
        CartItemResponseDto data = cartService.saveForLater(cartItemId);

        ApiResponse<CartItemResponseDto> response = ApiResponse.<CartItemResponseDto>builder()
                .success(true)
                .message("Đã lưu sản phẩm")
                .data(data)
                .timeStamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);

    }

    //todo: move cartItem to active
    @PutMapping("/{cartItemId}/move-to-active")
    public ResponseEntity<ApiResponse<CartItemResponseDto>> moveToActive(
            @PathVariable UUID cartItemId
    ){

        log.info("Moving item to active: {}",cartItemId);
        CartItemResponseDto data = cartService.moveToActive(cartItemId);

        ApiResponse<CartItemResponseDto> response = ApiResponse.<CartItemResponseDto>builder()
                .success(true)
                .message("Đã chuyển sản phẩm về giỏ hàng")
                .data(data)
                .timeStamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }



}
