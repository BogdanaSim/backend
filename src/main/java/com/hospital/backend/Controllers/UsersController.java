package com.hospital.backend.Controllers;

import com.hospital.backend.Converters.UserConverter;
import com.hospital.backend.DTOs.ShiftDTO;
import com.hospital.backend.DTOs.UserDTO;
import com.hospital.backend.Models.Shift;
import com.hospital.backend.Models.User;
import com.hospital.backend.Services.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UsersController {

    private final UsersService usersService;
    private final UserConverter userConverter;

    @GetMapping("getAllUsers")
    public List<UserDTO> findAllUsers() {
        List<User> users = usersService.getAllUsers();
        return userConverter.convertModelListToDtoList(users);
    }

    @PutMapping("/updateUser")
    public UserDTO updateUser(@RequestBody UserDTO userDTO) {
        User user = userConverter.convertDtoToModel(userDTO);
        User updatedUser = usersService.update(user);
        return userConverter.convertModelToDto(updatedUser);
    }

    @DeleteMapping("/deleteUser/{id}")
    public void deleteShift(@PathVariable Long id) {
        usersService.deleteById(id);
    }
}
