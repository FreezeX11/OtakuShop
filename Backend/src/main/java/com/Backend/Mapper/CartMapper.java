package com.Backend.Mapper;

import com.Backend.Entity.Cart;
import com.Backend.Payload.Response.CartResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CartMapper {
    private final CartItemMapper cartItemMapper;

    public CartResponse toCartResponse(Cart cart) {
        CartResponse cartResponse = new CartResponse();

        cartResponse.setId(cart.getId());
        cartResponse.setCartItemResponses(
                cart.getCartItems().stream()
                        .map(cartItemMapper::toCartItemResponse)
                        .toList()
        );

        cartResponse.setTotalPrice(cart.getTotalPrice());

        return cartResponse;
    }
}
