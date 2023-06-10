package com.hospital.backend.Converters;

import com.hospital.backend.DTOs.DepartmentDTO;
import com.hospital.backend.DTOs.UserDTO;
import com.hospital.backend.Models.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserConverter implements IConverter<User, UserDTO>{
    @Override
    public User convertDtoToModel(UserDTO userDTO) {
        User user = new User();
        if(userDTO.getId()!=null)
            user.setId(userDTO.getId());
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setRoleStaff(RoleStaff.valueOf(userDTO.getRoleStaff()));
        user.setRoleUser(RoleUser.valueOf(userDTO.getRoleUser()));
        Department department = new Department();
        department.setId(userDTO.getDepartmentId());
        user.setDepartment(department);
        user.setVacationDays(userDTO.getVacationDays());
        return user;
    }

    @Override
    public UserDTO convertModelToDto(User user) {
        UserDTO userDTO = new UserDTO();
        if(user.getId() == null)
            return userDTO;
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setRoleStaff(user.getRoleStaff().toString());
        userDTO.setRoleUser(user.getRoleUser().toString());
        userDTO.setDepartmentId(user.getDepartment().getId());
        userDTO.setVacationDays(user.getVacationDays());
        return userDTO;
    }

    @Override
    public List<UserDTO> convertModelListToDtoList(List<User> users) {
        return users
                .stream()
                .map(this::convertModelToDto)
                .collect(Collectors.toList());
    }
}
