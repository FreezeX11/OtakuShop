package com.Backend.Mapper;

import com.Backend.Entity.CartItem;
import com.Backend.Payload.Request.CartItemRequest;
import com.Backend.Payload.Response.CartItemResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class CartItemMapper {
    private final ProductMapper productMapper;
    private final VariationValueMapper variationValueMapper;
    private final ProductSkuMapper productSkuMapper;

    public CartItem toCartItem(CartItemRequest cartItemRequest) {
        CartItem cartItem = new CartItem();

        cartItem.setQuantity(cartItemRequest.getQuantity());
        cartItem.setCreatedDate(LocalDateTime.now());

        return cartItem;
    }

    public CartItemResponse toCartItemResponse(CartItem cartItem) {
        CartItemResponse cartItemResponse = new CartItemResponse();

        cartItemResponse.setId(cartItem.getId());
        cartItemResponse.setQuantity(cartItem.getQuantity());
        cartItemResponse.setProductResponse(productMapper.toProductResponse(cartItem.getProductSku().getProduct()));
        cartItemResponse.setProductSkuResponse(productSkuMapper.toProductSkuResponse(cartItem.getProductSku()));
        cartItemResponse.setQuantity(cartItem.getQuantity());

        return cartItemResponse;
    }
}
