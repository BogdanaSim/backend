package com.hospital.backend.Converters;

import com.hospital.backend.DTOs.ShiftDTO;
import com.hospital.backend.Models.Day;
import com.hospital.backend.Models.Schedule;
import com.hospital.backend.Models.Shift;
import com.hospital.backend.Models.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ShiftConverter implements IConverter<Shift, ShiftDTO>{
    @Override
    public Shift convertDtoToModel(ShiftDTO shiftDTO) {
        Shift shift = new Shift();
        if(shiftDTO.getId()!=null)
            shift.setId(shiftDTO.getId());
        shift.setType(shiftDTO.getType());
        User user = new User();
        user.setId(shiftDTO.getUserId());
        shift.setUser(user);
        Day day =new Day();
        day.setId(shiftDTO.getDayId());
        shift.setDay(day);
        return shift;
    }

    @Override
    public ShiftDTO convertModelToDto(Shift shift) {
        ShiftDTO shiftDTO = new ShiftDTO();
        if(shift.getId() == null)
            return shiftDTO;
        shiftDTO.setId(shift.getId());
        shiftDTO.setType(shift.getType());
        shiftDTO.setUserId(shift.getUser().getId());
        shiftDTO.setDayId(shift.getDay().getId());
        return shiftDTO;
    }

    @Override
    public List<ShiftDTO> convertModelListToDtoList(List<Shift> shifts) {
        return shifts
                .stream()
                .map(this::convertModelToDto)
                .collect(Collectors.toList());
    }

}
