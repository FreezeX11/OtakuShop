package com.Backend.Repository;

import com.Backend.Entity.Category;
import com.Backend.Entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    Optional<Category> findByNameIgnoreCase(String name);
}
