package com.Backend.Util;

import com.Backend.Entity.Cart;
import com.Backend.Entity.CartItem;
import com.Backend.Entity.Product;
import com.Backend.Repository.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@AllArgsConstructor
public class CartUtil {
    private final CartRepository cartRepository;

    public Cart recalculateCartTotalForUpdatedItem(CartItem cartItem, Product product) {
        Cart cart = cartItem.getCart();

        BigDecimal total = cart.getTotalPrice();

        BigDecimal oldItemTotal = cartItem.getPrice()
                .multiply(BigDecimal.valueOf(cartItem.getQuantity()));

        total = total.subtract(oldItemTotal);

        cartItem.setPrice(product.getPrice());

        BigDecimal newItemTotal = cartItem.getPrice().
                multiply(BigDecimal.valueOf(cartItem.getQuantity()));

        total = total.add(newItemTotal);

        cart.setTotalPrice(total);

        return cart;
    }
}
