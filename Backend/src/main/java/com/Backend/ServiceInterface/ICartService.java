package com.Backend.ServiceInterface;

import com.Backend.Entity.User;
import com.Backend.Payload.Request.CartItemRequest;
import com.Backend.Payload.Response.CartResponse;

public interface ICartService {
    void createCart(User user);
    void addProductInCart(CartItemRequest cartItemRequest);
    void updateProductQuantityInCart(Long productSkuId, int quantity);
    void deleteProductInCart(Long productSkuId);
    void updateProductInCart(Long productId);
    CartResponse getCart();
}
