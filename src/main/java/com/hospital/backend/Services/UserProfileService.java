package com.hospital.backend.Services;

import com.hospital.backend.DTOs.UserProfileDTO;
import com.hospital.backend.Exceptions.*;
import com.hospital.backend.Models.User;
import com.hospital.backend.Models.UserProfile;
import com.hospital.backend.Repositories.UsersProfilesRepository;
import com.hospital.backend.Repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserProfileService implements IUserProfileService{

    private final UsersProfilesRepository userProfileRepository;

    private final UsersRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserProfileService.class);

    @Override
    public UserProfile save(UserProfile userProfile) {
        logger.info(String.format("Saving user profile with id %d", userProfile.getId()));
        User user = this.userRepository.findById(userProfile.getId()).orElseThrow(UserNotFoundException::new);
        this.userRepository.save(user);
        userProfile.setUser(user);
        return this.userProfileRepository.save(userProfile);
    }

    @Override
    public void deleteById(Long id) {
        logger.info(String.format("Deleting user profile with id %d", id));
        this.userProfileRepository.findById(id).orElseThrow(UserProfileNotFoundException::new);
        this.userProfileRepository.deleteById(id);
    }

    @Override
    public UserProfile findByUserId(Long id) {
        UserProfile userProfile =this.userProfileRepository.findByUserId(id);
        return this.userProfileRepository.findByUserId(id);
    }

    @Override
    public UserProfile update(UserProfile userProfile) {
        logger.info(String.format("Updating user profile with id %d", userProfile.getId()));
        User user = this.userRepository.findById(userProfile.getId()).orElseThrow(UserNotFoundException::new);
        this.userProfileRepository.findById(userProfile.getId()).orElseThrow(UserProfileNotFoundException::new);
        userProfile.setUser(user);
        return this.userProfileRepository.save(userProfile);
    }

    @Override
    public UserProfile findByEmail(String email) {
        logger.info(String.format("Finding user profile with email %s", email));
        User user = this.userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        return this.userProfileRepository.findByUserId(user.getId());
    }

    public UserProfile removeProfilePicture(Long idProfile){
        UserProfile userProfile = this.userProfileRepository.findById(idProfile).orElseThrow(UserProfileNotFoundException::new);
        userProfile.setProfilePicture(null);
        return this.userProfileRepository.save(userProfile);
    }
}
