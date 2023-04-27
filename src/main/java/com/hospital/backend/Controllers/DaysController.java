package com.hospital.backend.Controllers;

import com.hospital.backend.Converters.DayConverter;
import com.hospital.backend.Converters.ScheduleConverter;
import com.hospital.backend.DTOs.ScheduleDTO;
import com.hospital.backend.Models.Schedule;
import com.hospital.backend.Services.DaysService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DaysController {

    private final DaysService daysService;

    private final DayConverter dayConverter;

    private final ScheduleConverter scheduleConverter;


}
