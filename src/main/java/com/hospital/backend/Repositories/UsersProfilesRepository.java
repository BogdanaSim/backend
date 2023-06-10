package com.hospital.backend.Repositories;

import com.hospital.backend.Models.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersProfilesRepository extends JpaRepository<UserProfile, Long> {
    UserProfile findByUserId(Long user_id);

    Optional<UserProfile> findById(Long id);
}
