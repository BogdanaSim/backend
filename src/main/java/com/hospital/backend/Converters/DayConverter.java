package com.hospital.backend.Converters;

import com.hospital.backend.DTOs.DayDTO;
import com.hospital.backend.DTOs.ScheduleDTO;
import com.hospital.backend.Models.Day;
import com.hospital.backend.Models.Schedule;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DayConverter implements IConverter<Day, DayDTO>{
    @Override
    public Day convertDtoToModel(DayDTO dayDTO) {
        Day day = new Day();
        if(dayDTO.getId()!=null)
            day.setId(dayDTO.getId());
        day.setDate(dayDTO.getDate());
        Schedule schedule = new Schedule();
        schedule.setId(dayDTO.getScheduleId());
        day.setSchedule(schedule);
        return day;
    }

    @Override
    public DayDTO convertModelToDto(Day day) {
        DayDTO dayDTO = new DayDTO();
        if(day.getId() == null)
            return dayDTO;
        dayDTO.setId(day.getId());
        dayDTO.setDate(day.getDate());
        dayDTO.setScheduleId(day.getSchedule().getId());
        return dayDTO;
    }

    @Override
    public List<DayDTO> convertModelListToDtoList(List<Day> days) {
        return days
                .stream()
                .map(this::convertModelToDto)
                .collect(Collectors.toList());
    }
}
