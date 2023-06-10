package com.hospital.backend.Converters;

import com.hospital.backend.DTOs.UserDTO;
import com.hospital.backend.DTOs.UserProfileDTO;
import com.hospital.backend.Models.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
@Component
public class UserProfileConverter  implements IConverter<UserProfile, UserProfileDTO>{
    @Override
    public UserProfile convertDtoToModel(UserProfileDTO userProfileDTO) {
        UserProfile user = new UserProfile();
        if(userProfileDTO.getId()!=null)
            user.setId(userProfileDTO.getId());
        user.setPhoneNumber(userProfileDTO.getPhoneNumber());
        user.setDetails(userProfileDTO.getDetails());
        user.setProfilePicture(userProfileDTO.getProfilePicture());
        User user1 = new User();
        user1.setId(userProfileDTO.getUserId());
        user.setUser(user1);
        return user;    }

    @Override
    public UserProfileDTO convertModelToDto(UserProfile userProfile) {
        UserProfileDTO userDTO = new UserProfileDTO();
        if(userProfile.getId() == null)
            return userDTO;
        userDTO.setId(userProfile.getId());
        userDTO.setDetails(userProfile.getDetails());
        userDTO.setPhoneNumber(userProfile.getPhoneNumber());
        userDTO.setProfilePicture(userProfile.getProfilePicture());
        userDTO.setUserId(userProfile.getUser().getId());
        return userDTO;
    }

    @Override
    public List<UserProfileDTO> convertModelListToDtoList(List<UserProfile> userProfiles) {
        return userProfiles
                .stream()
                .map(this::convertModelToDto)
                .collect(Collectors.toList());
    }
}
