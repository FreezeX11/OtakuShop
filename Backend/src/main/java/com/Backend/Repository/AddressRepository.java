package com.Backend.Repository;

import com.Backend.Entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    boolean existsByDistrictAndLandmarkIgnoreCaseAndUserId(
            String district,
            String landmark,
            Long userId
    );
    List<Address> findByUserId(Long userId);
}
