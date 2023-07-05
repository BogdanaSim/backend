package com.hospital.backend.Controllers;

import com.hospital.backend.Converters.DepartmentConverter;
import com.hospital.backend.Converters.ScheduleConverter;
import com.hospital.backend.DTOs.ScheduleDTO;
import com.hospital.backend.Models.Schedule;
import com.hospital.backend.Services.ScheduleService;
import com.hospital.backend.Services.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
//@CrossOrigin
@RequestMapping("/api/schedules")
@CrossOrigin(origins  ={ "http://localhost:4200","https://hssh.azurewebsites.net" }, maxAge = 3600, allowCredentials="true")
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
        return scheduleDTO;
    }

    @PostMapping("/generateNew12hDaysSchedule")
    public ScheduleDTO generateNew12hDaysSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = scheduleConverter.convertDtoToModel(scheduleDTO);
        Schedule newSchedule = scheduleService.generateNew12hDaysSchedule(schedule,usersService.getUserByRoleAndDepartment(schedule.getRoleStaff().toString(),scheduleDTO.getDepartment().getId()));
//        System.out.println(        newSchedule.toString(usersService.getUserByRoleAndDepartment(schedule.getRoleStaff().toString(),scheduleDTO.getDepartment().getId())));
        return scheduleConverter.convertModelToDto(newSchedule);
    }

    @PostMapping("/generateNew8hDaysSchedule")
    public ScheduleDTO generateNew8hDaysSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = scheduleConverter.convertDtoToModel(scheduleDTO);
        Schedule newSchedule = scheduleService.generateNew8hDaysSchedule(schedule,usersService.getUserByRoleAndDepartment(schedule.getRoleStaff().toString(),scheduleDTO.getDepartment().getId()));
//        System.out.println(schedule.toString(usersService.getUserByRoleAndDepartment(schedule.getRoleStaff().toString(),scheduleDTO.getDepartment().getId())));
        return scheduleConverter.convertModelToDto(newSchedule);
    }

    @PostMapping("/addSchedule")
    public ScheduleDTO addUser(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = scheduleConverter.convertDtoToModel(scheduleDTO);
        Schedule addedSchedule = scheduleService.save(schedule);
        return scheduleConverter.convertModelToDto(addedSchedule);
    }
    @PutMapping("/updateSchedule")
    public ScheduleDTO updateSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = scheduleConverter.convertDtoToModel(scheduleDTO);
        Schedule addedSchedule = scheduleService.update(schedule);
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

    @GetMapping("/findScheduleByDateAndDepartmentAndRoleStaffAndScheduleStatus/{date}/{departmentId}/{roleStaff}/{scheduleStatus}")
    public ScheduleDTO findScheduleByDateAndDepartmentAndRoleStaffAndScheduleStatus(@PathVariable Long departmentId, @PathVariable LocalDate date, @PathVariable String roleStaff, @PathVariable String scheduleStatus){
        Schedule schedule = scheduleService.findSchedulesByDateAndDepartmentAndRoleStaffAndScheduleStatus(date,departmentId,roleStaff,scheduleStatus);
        return scheduleConverter.convertModelToDto(schedule);
    }
}
