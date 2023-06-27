package com.hospital.backend.Controllers;

import com.hospital.backend.Converters.UserConverter;
import com.hospital.backend.DTOs.ShiftDTO;
import com.hospital.backend.DTOs.UserDTO;
import com.hospital.backend.Models.Shift;
import com.hospital.backend.Models.User;
import com.hospital.backend.Services.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@CrossOrigin
@RequestMapping("/api/users")
@CrossOrigin(origins  ={ "http://localhost:4200","https://hssh.azurewebsites.net" }, maxAge = 3600, allowCredentials="true")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UsersController {

    private final UsersService usersService;
    private final UserConverter userConverter;


    @GetMapping("/getAllUsers")
    public List<UserDTO> findAllUsers() {
        List<User> users = usersService.getAllUsers();
        return userConverter.convertModelListToDtoList(users);
    }

    @GetMapping("/findByRoleAndDepartment/{role}/{departmentId}")
    public List<UserDTO> findByRoleAndDepartment(@PathVariable Long departmentId, @PathVariable String role) {
        List<User> users = usersService.getUserByRoleAndDepartment(role,departmentId);
        return userConverter.convertModelListToDtoList(users);
    }

    @GetMapping("/findByDepartment/{id}")
    public List<UserDTO> findByDepartment(@PathVariable Long id) {
        List<User> users = usersService.findUserByDepartment(id);
        return userConverter.convertModelListToDtoList(users);
    }

    @PutMapping("/updateUser")
    public UserDTO updateUser(@RequestBody UserDTO userDTO) {
        User user = userConverter.convertDtoToModel(userDTO);
        User updatedUser = usersService.update(user);
        return userConverter.convertModelToDto(updatedUser);
    }
    @GetMapping("/findUserById/{userId}")
    public UserDTO findUserById(@PathVariable Long userId) {
        User user =usersService.findById(userId);
        return userConverter.convertModelToDto(user);
    }

    @GetMapping("/findUserByEmail/{email}")
    public UserDTO findUserByEmail(@PathVariable String email) {
        User user =usersService.findByEmail(email);
        return userConverter.convertModelToDto(user);
    }

    @DeleteMapping("/deleteUser/{id}")
    public void deleteShift(@PathVariable Long id) {
        usersService.deleteById(id);
    }
}
