package com.Backend.ServiceInterface;

import com.Backend.Entity.User;
import com.Backend.Payload.Request.CartItemRequest;

public interface ICartService {
    void createCart(User user);
    void addProductInCart(CartItemRequest cartItemRequest);
    void updateProductQuantityInCart(int quantity);
    void deleteProductInCart();
}
