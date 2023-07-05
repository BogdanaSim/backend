package com.hospital.backend.Controllers;

import com.hospital.backend.Configurations.JWT.JwtUtils;
import com.hospital.backend.Configurations.Services.UserDetailsImpl;
import com.hospital.backend.DTOs.LoginRequestDTO;
import com.hospital.backend.DTOs.MessageResponseDTO;
import com.hospital.backend.DTOs.SignupRequestDTO;
import com.hospital.backend.DTOs.UserInfoResponseDTO;
import com.hospital.backend.Models.RoleStaff;
import com.hospital.backend.Models.RoleUser;
import com.hospital.backend.Models.User;
import com.hospital.backend.Models.UserProfile;
import com.hospital.backend.Repositories.UsersProfilesRepository;
import com.hospital.backend.Repositories.UsersRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins ={ "http://localhost:4200","https://hssh.azurewebsites.net" }, maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    UsersProfilesRepository usersProfilesRepository;



    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signIn")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new UserInfoResponseDTO(userDetails.getId(),
                        userDetails.getEmail(),
                        roles.toString()));
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequestDTO signUpRequest) {
        if (usersRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponseDTO("Error: Email is already taken!"));
        }


        // Create new user's account
        User user = new User(signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()), signUpRequest.getRoleUser(), signUpRequest.getRoleStaff(),signUpRequest.getFirstName(),signUpRequest.getLastName(), signUpRequest.getIdDepartment());
        user.setRoleUser(RoleUser.USER);
        User user1 = usersRepository.save(user);
        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user1);
        usersProfilesRepository.save(userProfile);
        return ResponseEntity.ok(new MessageResponseDTO("User registered successfully!"));
    }

    @PostMapping("/signOut")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponseDTO("You've been signed out!"));
    }
}