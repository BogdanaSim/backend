package com.hospital.backend.Controllers;

import com.hospital.backend.Converters.DepartmentConverter;
import com.hospital.backend.Services.DepartmentsService;
import com.hospital.backend.Services.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DepartmentsController {

    private final DepartmentsService departmentsService;

    private final ScheduleService scheduleService;

    private final DepartmentConverter departmentConverter;


}
