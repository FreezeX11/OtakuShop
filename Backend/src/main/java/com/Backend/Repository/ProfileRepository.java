package com.Backend.Repository;

import com.Backend.Entity.Profile;
import com.Backend.Enumeration.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUserProfile(UserProfile userProfile);
}
