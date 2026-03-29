package com.Backend.Repository;

import com.Backend.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByNameIgnoreCase(String name);
    List<Product> findByEnable(boolean enable);
    List<Product> findByEnableAndSubCategoryId(boolean enable, Long subCategoryId);
}
