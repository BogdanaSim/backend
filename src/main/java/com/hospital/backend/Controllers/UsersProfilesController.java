package com.hospital.backend.Controllers;

import com.hospital.backend.Converters.UserProfileConverter;
import com.hospital.backend.DTOs.DepartmentDTO;
import com.hospital.backend.DTOs.UserDTO;
import com.hospital.backend.DTOs.UserProfileDTO;
import com.hospital.backend.Models.Department;
import com.hospital.backend.Models.User;
import com.hospital.backend.Models.UserProfile;
import com.hospital.backend.Services.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
//@CrossOrigin
@RequestMapping("/api/profiles")
@CrossOrigin(origins  ={ "http://localhost:4200","https://hssh.azurewebsites.net" }, maxAge = 3600, allowCredentials="true")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UsersProfilesController {

    private final UserProfileService userProfileService;
    private final UserProfileConverter userProfileConverter;

    @GetMapping("/findProfileByUserId/{userId}")
    public UserProfileDTO findProfileByUserId(@PathVariable Long userId) {
        UserProfile userProfile = userProfileService.findByUserId(userId);
        return userProfileConverter.convertModelToDto(userProfile);
    }

    @PutMapping("/updateUserProfile")
    public UserProfileDTO updateUserProfile(@RequestBody UserProfileDTO userProfileDTO) {
        UserProfile userProfile = userProfileConverter.convertDtoToModel(userProfileDTO);
        UserProfile updatedUserProfile = userProfileService.update(userProfile);
        return userProfileConverter.convertModelToDto(updatedUserProfile);
    }
    @PutMapping(path = "/updateUserProfilePicture/{userId}")
    public UserProfileDTO updateUserProfilePicture(@RequestParam(name = "profilePicture") MultipartFile multipartFile, @PathVariable Long userId) throws IOException {
        UserProfile userProfile = userProfileService.findByUserId(userId);
        userProfile.setProfilePicture(multipartFile.getBytes());
        return this.userProfileConverter.convertModelToDto(this.userProfileService.update(userProfile));
    }

    @GetMapping(path = "/removeProfilePicture/{profile_Id}")
    public UserProfileDTO removeProfilePicture(@PathVariable Long profile_Id) {
        return this.userProfileConverter.convertModelToDto(this.userProfileService.removeProfilePicture(profile_Id));
    }

}