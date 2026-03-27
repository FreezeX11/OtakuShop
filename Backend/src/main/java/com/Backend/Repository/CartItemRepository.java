package com.Backend.Repository;

import com.Backend.Entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    boolean existsByProductSkuIdAndCartId(Long productSkuId, Long cartId);
    Optional<CartItem> findByProductSkuIdAndCartId(Long productSkuId, Long cartId);
    List<CartItem> findByProductSkuId(Long productSkuId);
    List<CartItem> findByProductSkuIdIn(List<Long> productSkusIds);
}
