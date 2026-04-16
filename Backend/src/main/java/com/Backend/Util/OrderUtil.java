package com.Backend.Util;

import com.Backend.Entity.CartItem;
import com.Backend.Entity.OrderItem;
import com.Backend.Entity.ProductSku;
import com.Backend.Repository.ProductSkuRepository;
import com.Backend.Service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
public class OrderUtil {
    private final CartService cartService;
    private final ProductSkuRepository productSkuRepository;

    public void updateStockAndCart(List<CartItem> cartItems) {
        cartItems.forEach(cartItem -> {
            ProductSku existingProductSku = cartItem.getProductSku();

            existingProductSku.setQuantity(existingProductSku.getQuantity() - cartItem.getQuantity());
            cartService.deleteProductInCart(existingProductSku.getId());
            productSkuRepository.save(existingProductSku);
        });
    }
}
