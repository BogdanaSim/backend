package com.hospital.backend.Controllers;

import com.hospital.backend.Converters.ScheduleConverter;
import com.hospital.backend.DTOs.ScheduleDTO;
import com.hospital.backend.Models.Schedule;
import com.hospital.backend.Services.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SchedulesController {

    private final ScheduleService scheduleService;

    private final ScheduleConverter scheduleConverter;

    @PostMapping("/addNewDaysSchedule")
    public ScheduleDTO addNewDaysSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = scheduleConverter.convertDtoToModel(scheduleDTO);
        scheduleService.addNewDaysSchedule(schedule);
        return scheduleDTO;
    }

    @PostMapping("/addSchedule")
    public ScheduleDTO addUser(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = scheduleConverter.convertDtoToModel(scheduleDTO);
        Schedule addedSchedule = scheduleService.save(schedule);
        return scheduleConverter.convertModelToDto(addedSchedule);
    }
}
