package com.Backend.Repository;

import com.Backend.Entity.VariationValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VariationValueRepository extends JpaRepository<VariationValue, Long> {
    Optional<VariationValue> findByNameIgnoreCase(String name);
}
