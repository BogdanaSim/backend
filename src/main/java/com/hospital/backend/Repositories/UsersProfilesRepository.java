package com.hospital.backend.Repositories;

import com.hospital.backend.Models.User;
import com.hospital.backend.Models.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersProfilesRepository extends JpaRepository<UserProfile, Long> {
    UserProfile findByUserId(Long user_id);
}
