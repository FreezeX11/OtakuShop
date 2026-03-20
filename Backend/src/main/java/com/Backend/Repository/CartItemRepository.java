package com.Backend.Repository;

import com.Backend.Entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    boolean existsByProductSkuIdAndCartId(Long productSkuId, Long cartId);
}
