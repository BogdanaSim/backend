package com.hospital.backend.Controllers;

import com.hospital.backend.Converters.DepartmentConverter;
import com.hospital.backend.Converters.ScheduleConverter;
import com.hospital.backend.DTOs.ScheduleDTO;
import com.hospital.backend.DTOs.ShiftDTO;
import com.hospital.backend.Exceptions.InvalidSolutionException;
import com.hospital.backend.Models.Department;
import com.hospital.backend.Models.Schedule;
import com.hospital.backend.Models.Shift;
import com.hospital.backend.Services.ScheduleService;
import com.hospital.backend.Services.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SchedulesController {

    private final ScheduleService scheduleService;

    private final UsersService usersService;

    private final ScheduleConverter scheduleConverter;

    private final DepartmentConverter departmentConverter;

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
        Schedule newSchedule = scheduleService.generateNew12hDaysSchedule(schedule,usersService.getUserByRoleAndDepartment(schedule.getRoleStaff().toString(),scheduleDTO.getDepartment().getId()));
//        System.out.println(        newSchedule.toString(usersService.getUserByRoleAndDepartment(schedule.getRoleStaff().toString(),scheduleDTO.getDepartment().getId())));
        //        while (true){
//            try{
//                schedule.setDays(scheduleService.generateNew12hDaysSchedule(schedule,usersService.getAllUsersWithoutShifts()));
//                break;
//            } catch (InvalidSolutionException ex) {
//                System.out.println("Invalid solution exception occurred: " + ex.getMessage());
//            } catch (RuntimeException ex) {
//                System.out.println("Exception occurred: " + ex.getMessage());
//            }
//        }
//        schedule.setDays(scheduleService.generateNew12hDaysSchedule(schedule,usersService.getAllUsersWithoutShifts()));

//        System.out.println(schedule.toString(usersService.getAllUsers()));
        return scheduleConverter.convertModelToDto(newSchedule);
    }

    @PostMapping("/addSchedule")
    public ScheduleDTO addUser(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = scheduleConverter.convertDtoToModel(scheduleDTO);
        Schedule addedSchedule = scheduleService.save(schedule);
        return scheduleConverter.convertModelToDto(addedSchedule);
    }

    @DeleteMapping("/deleteSchedule/{id}")
    public void deleteShift(@PathVariable Long id) {
        scheduleService.deleteById(id);
    }

    @GetMapping("/findScheduleById/{scheduleId}")
    public ScheduleDTO findScheduleById(@PathVariable Long scheduleId) {
        Schedule schedule = scheduleService.findById(scheduleId);
        return scheduleConverter.convertModelToDto(schedule);
    }

    @GetMapping("/getDatesByDepartmentId/{departmentId}")
    public List<LocalDate> getDatesByDepartmentId(@PathVariable Long departmentId) {
        return scheduleService.getDatesByDepartment(departmentId);
    }

    @GetMapping("/findScheduleByDateAndDepartment/{date}/{departmentId}")
    public ScheduleDTO findScheduleByDateAndDepartment(@PathVariable Long departmentId, @PathVariable LocalDate date){
        Schedule schedule = scheduleService.findScheduleByDateAndDepartment(date,departmentId);
        return scheduleConverter.convertModelToDto(schedule);
    }

    @GetMapping("/findScheduleByDateAndDepartmentAndRoleStaff/{date}/{departmentId}/{roleStaff}")
    public ScheduleDTO findScheduleByDateAndDepartmentAndRoleStaff(@PathVariable Long departmentId, @PathVariable LocalDate date, @PathVariable String roleStaff){
        Schedule schedule = scheduleService.findScheduleByDateAndDepartmentAndRoleStaff(date,departmentId,roleStaff);
        return scheduleConverter.convertModelToDto(schedule);
    }
}
