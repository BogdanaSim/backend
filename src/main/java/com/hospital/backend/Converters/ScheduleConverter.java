package com.hospital.backend.Converters;

import com.hospital.backend.DTOs.DepartmentDTO;
import com.hospital.backend.DTOs.ScheduleDTO;
import com.hospital.backend.Models.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ScheduleConverter implements IConverter<Schedule, ScheduleDTO>{
    @Override
    public Schedule convertDtoToModel(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        if(scheduleDTO.getId()!=null)
            schedule.setId(scheduleDTO.getId());
        schedule.setDate(scheduleDTO.getDate());
        schedule.setRoleStaff(RoleStaff.valueOf(scheduleDTO.getRoleStaff()));
        schedule.setScheduleStatus(ScheduleStatus.valueOf(scheduleDTO.getStatus()));
        Department department = new Department();
        department.setId(scheduleDTO.getDepartment().getId());
        department.setName(scheduleDTO.getDepartment().getName());
        schedule.setDepartment(department);
        schedule.setScheduleType(ScheduleType.valueOf(scheduleDTO.getType()));
        return schedule;
    }

    @Override
    public ScheduleDTO convertModelToDto(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        DepartmentDTO departmentDTO=new DepartmentDTO();
        if(schedule.getId() == null)
            return scheduleDTO;
        scheduleDTO.setId(schedule.getId());
        scheduleDTO.setDate(schedule.getDate());
        scheduleDTO.setRoleStaff(schedule.getRoleStaff().toString());
        scheduleDTO.setStatus(schedule.getScheduleStatus().toString());
        departmentDTO.setId(schedule.getDepartment().getId());
        departmentDTO.setName(schedule.getDepartment().getName());
        scheduleDTO.setDepartment(departmentDTO);
        scheduleDTO.setType(schedule.getScheduleType().toString());
        return scheduleDTO;
    }

    @Override
    public List<ScheduleDTO> convertModelListToDtoList(List<Schedule> schedules) {
        return schedules
                .stream()
                .map(this::convertModelToDto)
                .collect(Collectors.toList());
    }
}
