package com.Backend.Repository;

import com.Backend.Entity.ProductSku;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductSkuRepository extends JpaRepository<ProductSku, Long> {
}
