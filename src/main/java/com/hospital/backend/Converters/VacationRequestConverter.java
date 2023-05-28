package com.hospital.backend.Converters;

import com.hospital.backend.DTOs.VacationRequestDTO;
import com.hospital.backend.Models.StatusRequest;
import com.hospital.backend.Models.User;
import com.hospital.backend.Models.VacationRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
@Component
public class VacationRequestConverter implements IConverter<VacationRequest, VacationRequestDTO>{
    @Override
    public VacationRequest convertDtoToModel(VacationRequestDTO vacationRequestDTO) {
        VacationRequest vacationRequest = new VacationRequest();
        if(vacationRequestDTO.getId()!=null){
            vacationRequest.setId(vacationRequestDTO.getId());

        }
        vacationRequest.setType(vacationRequestDTO.getType());
        vacationRequest.setStartDate(vacationRequestDTO.getStartDate());
        vacationRequest.setEndDate(vacationRequestDTO.getEndDate());
        vacationRequest.setStatus(StatusRequest.valueOf(vacationRequestDTO.getStatus()));
        User user =new User();
        user.setId(vacationRequestDTO.getUserId());
        vacationRequest.setUser(user);
        return vacationRequest;
    }

    @Override
    public VacationRequestDTO convertModelToDto(VacationRequest vacationRequest) {
        VacationRequestDTO vacationRequestDTO = new VacationRequestDTO();
        if(vacationRequest==null){
                return vacationRequestDTO;
        }
        vacationRequestDTO.setId(vacationRequest.getId());
        vacationRequestDTO.setType(vacationRequest.getType());
        vacationRequestDTO.setStartDate(vacationRequest.getStartDate());
        vacationRequestDTO.setEndDate(vacationRequest.getEndDate());
        vacationRequestDTO.setStatus(vacationRequest.getStatus().toString());
        vacationRequestDTO.setUserId(vacationRequest.getUser().getId());
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
