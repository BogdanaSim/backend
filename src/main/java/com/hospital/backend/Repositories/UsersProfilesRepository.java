package com.hospital.backend.Repositories;

import com.hospital.backend.Models.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersProfilesRepository extends JpaRepository<UserProfile, Long> {
    UserProfile findByUserId(Long id);
}
