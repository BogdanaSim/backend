package com.hospital.backend.Controllers;

import com.hospital.backend.Converters.DepartmentConverter;
import com.hospital.backend.DTOs.DepartmentDTO;
import com.hospital.backend.DTOs.ScheduleDTO;
import com.hospital.backend.Models.Department;
import com.hospital.backend.Models.Schedule;
import com.hospital.backend.Services.DepartmentsService;
import com.hospital.backend.Services.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
//@CrossOrigin
@RequestMapping("/api/departments")
@CrossOrigin(origins  ={ "http://localhost:4200","https://hssh.azurewebsites.net" }, maxAge = 3600, allowCredentials="true")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DepartmentsController {

    private final DepartmentsService departmentsService;

    private final ScheduleService scheduleService;

    private final DepartmentConverter departmentConverter;

    @GetMapping("/findDepartmentById/{departmentId}")
    public DepartmentDTO findDepartmentById(@PathVariable Long departmentId) {
        Department department =departmentsService.findById(departmentId);
        return departmentConverter.convertModelToDto(department);
    }

}
