package com.Backend.Repository;

import com.Backend.Entity.Variation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VariationRepository extends JpaRepository<Variation, Long> {
    Optional<Variation> findByNameIgnoreCase(String name);
}
