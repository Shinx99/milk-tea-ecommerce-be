package com.asm.ecommerce.cart.service;

import com.asm.ecommerce.cart.dto.request.AddToCartRequestDto;
import com.asm.ecommerce.cart.dto.request.RemoveCartRequestDto;
import com.asm.ecommerce.cart.dto.request.UpdateCartRequestDto;
import com.asm.ecommerce.cart.dto.response.CartItemResponseDto;
import com.asm.ecommerce.cart.dto.response.CartSummaryResponseDto;

import java.util.UUID;

public interface CartService {

    //TODO: add to Cart
    CartItemResponseDto addToCart(UUID customerId, AddToCartRequestDto requestDto);

    //TODO: get List cart
    CartSummaryResponseDto getCartByUserId(UUID userId);

    //TODO: update Quantity
    CartItemResponseDto updateCartItem(UUID cartItemId, UpdateCartRequestDto request);

    //todo: delete cartItem
    void removeCartItems(RemoveCartRequestDto request);

    //todo: clear Cart
    void clearCart(UUID customerId);

    //todo: save for later
    CartItemResponseDto saveForLater(UUID cartItemId);

    //todo: change SaveForLater to active
    CartItemResponseDto moveToActive(UUID cartItemId);

    //todo: change state


    //todo: check date & delete


}
