package com.hospital.backend.Controllers;

import com.hospital.backend.Converters.UserConverter;
import com.hospital.backend.DTOs.UserDTO;
import com.hospital.backend.Models.User;
import com.hospital.backend.Services.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
