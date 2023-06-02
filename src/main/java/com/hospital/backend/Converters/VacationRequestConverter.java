package com.hospital.backend.Converters;

import com.hospital.backend.DTOs.UserDTO;
import com.hospital.backend.DTOs.VacationRequestDTO;
import com.hospital.backend.Models.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
@Component
public class VacationRequestConverter implements IConverter<VacationRequest, VacationRequestDTO>{
    @Override
    public VacationRequest convertDtoToModel(VacationRequestDTO vacationRequestDTO) {
        VacationRequest vacationRequest = new VacationRequest();
        User user = new User();
        if(vacationRequestDTO.getId()!=null){
            vacationRequest.setId(vacationRequestDTO.getId());

        }

        vacationRequest.setType(vacationRequestDTO.getType());
        vacationRequest.setStartDate(vacationRequestDTO.getStartDate());
        vacationRequest.setEndDate(vacationRequestDTO.getEndDate());
        vacationRequest.setStatus(StatusRequest.valueOf(vacationRequestDTO.getStatus()));
        user.setId(vacationRequestDTO.getUserDTO().getId());
        user.setEmail(vacationRequestDTO.getUserDTO().getEmail());
        user.setPassword(vacationRequestDTO.getUserDTO().getPassword());
        user.setFirstName(vacationRequestDTO.getUserDTO().getFirstName());
        user.setLastName(vacationRequestDTO.getUserDTO().getLastName());
        user.setRoleStaff(RoleStaff.valueOf(vacationRequestDTO.getUserDTO().getRoleStaff()));
        user.setRoleUser(RoleUser.valueOf(vacationRequestDTO.getUserDTO().getRoleUser()));
        user.setVacationDays(vacationRequestDTO.getUserDTO().getVacationDays());
        Department department=new Department();
        department.setId(vacationRequestDTO.getUserDTO().getDepartmentId());
        user.setDepartment(department);
        vacationRequest.setUser(user);
        return vacationRequest;
    }

    @Override
    public VacationRequestDTO convertModelToDto(VacationRequest vacationRequest) {
        VacationRequestDTO vacationRequestDTO = new VacationRequestDTO();
        UserDTO userDTO = new UserDTO();
        if(vacationRequest.getId()==null){
                return vacationRequestDTO;
        }
        vacationRequestDTO.setId(vacationRequest.getId());
        vacationRequestDTO.setType(vacationRequest.getType());
        vacationRequestDTO.setStartDate(vacationRequest.getStartDate());
        vacationRequestDTO.setEndDate(vacationRequest.getEndDate());
        vacationRequestDTO.setStatus(vacationRequest.getStatus().toString());
        userDTO.setId(vacationRequest.getUser().getId());
        userDTO.setEmail(vacationRequest.getUser().getEmail());
        userDTO.setPassword(vacationRequest.getUser().getPassword());
        userDTO.setFirstName(vacationRequest.getUser().getFirstName());
        userDTO.setLastName(vacationRequest.getUser().getLastName());
        userDTO.setRoleStaff(String.valueOf(vacationRequest.getUser().getRoleStaff()));
        userDTO.setRoleUser(String.valueOf(vacationRequest.getUser().getRoleUser()));
        userDTO.setDepartmentId(vacationRequest.getUser().getDepartment().getId());
        userDTO.setVacationDays(vacationRequest.getUser().getVacationDays());
        vacationRequestDTO.setUserDTO(userDTO);
        return vacationRequestDTO;
    }

    @Override
    public List<VacationRequestDTO> convertModelListToDtoList(List<VacationRequest> vacationRequests) {
        return vacationRequests
                .stream()
                .map(this::convertModelToDto)
                .collect(Collectors.toList());
    }
}
