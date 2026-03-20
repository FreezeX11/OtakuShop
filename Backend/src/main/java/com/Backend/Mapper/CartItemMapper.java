package com.Backend.Mapper;

import com.Backend.Entity.CartItem;
import com.Backend.Payload.Request.CartItemRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CartItemMapper {

    public CartItem toCartItem(CartItemRequest cartItemRequest) {
        CartItem cartItem = new CartItem();

        cartItem.setQuantity(cartItemRequest.getQuantity());
        cartItem.setCreatedDate(LocalDateTime.now());

        return cartItem;
    }
}
