package com.hospital.backend.Services;

import com.hospital.backend.Models.UserProfile;

public interface IUserProfileService {
    UserProfile save(UserProfile userProfile);

    void deleteById(Long id);

    UserProfile findByUserId(Long id);

    UserProfile update(UserProfile userProfile);

    UserProfile findByEmail(String username);
}
