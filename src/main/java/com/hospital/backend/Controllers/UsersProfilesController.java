package com.hospital.backend.Controllers;

import com.hospital.backend.Converters.UserProfileConverter;
import com.hospital.backend.DTOs.DepartmentDTO;
import com.hospital.backend.DTOs.UserProfileDTO;
import com.hospital.backend.Models.Department;
import com.hospital.backend.Models.UserProfile;
import com.hospital.backend.Services.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
//@CrossOrigin
@RequestMapping("/api/profiles")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UsersProfilesController {

    private final UserProfileService userProfileService;
    private final UserProfileConverter userProfileConverter;

    @GetMapping("/findProfileByUserId/{userId}")
    public UserProfileDTO findProfileByUserId(@PathVariable Long userId) {
        UserProfile userProfile =userProfileService.findByUserId(userId);
        return userProfileConverter.convertModelToDto(userProfile);
    }}
