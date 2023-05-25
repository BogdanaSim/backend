package com.hospital.backend.Controllers;

import com.hospital.backend.Converters.ScheduleConverter;
import com.hospital.backend.DTOs.ScheduleDTO;
import com.hospital.backend.DTOs.ShiftDTO;
import com.hospital.backend.Models.Schedule;
import com.hospital.backend.Models.Shift;
import com.hospital.backend.Services.ScheduleService;
import com.hospital.backend.Services.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SchedulesController {

    private final ScheduleService scheduleService;

    private final UsersService usersService;

    private final ScheduleConverter scheduleConverter;

    @PostMapping("/addNewDaysSchedule")
    public ScheduleDTO addNewDaysSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = scheduleConverter.convertDtoToModel(scheduleDTO);
        scheduleService.addNewDaysSchedule(schedule);
        return scheduleDTO;
    }

    @PostMapping("/getNewDaysSchedule")
    public ScheduleDTO getNewDaysSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = scheduleConverter.convertDtoToModel(scheduleDTO);
        schedule.setDays(scheduleService.getNewDaysSchedule(schedule,usersService.getAllUsers().get(0)));
        System.out.println(schedule.toString(usersService.getAllUsers()));
        return scheduleDTO;
    }

    @PostMapping("/generateNew12hDaysSchedule")
    public ScheduleDTO generateNew12hDaysSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = scheduleConverter.convertDtoToModel(scheduleDTO);
        schedule.setDays(scheduleService.generateNew12hDaysSchedule(schedule,usersService.getAllUsersWithoutShifts()));
        System.out.println(schedule.toString(usersService.getAllUsers()));
        return scheduleDTO;
    }

    @PostMapping("/addSchedule")
    public ScheduleDTO addUser(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = scheduleConverter.convertDtoToModel(scheduleDTO);
        Schedule addedSchedule = scheduleService.save(schedule);
        return scheduleConverter.convertModelToDto(addedSchedule);
    }

    @GetMapping("/findScheduleById/{scheduleId}")
    public ScheduleDTO findScheduleById(@PathVariable Long scheduleId) {
        Schedule schedule = scheduleService.findById(scheduleId);
        return scheduleConverter.convertModelToDto(schedule);
    }
}
