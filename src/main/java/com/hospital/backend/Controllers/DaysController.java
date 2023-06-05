package com.hospital.backend.Controllers;

import com.hospital.backend.Converters.DayConverter;
import com.hospital.backend.Converters.ScheduleConverter;
import com.hospital.backend.DTOs.DayDTO;
import com.hospital.backend.DTOs.ScheduleDTO;
import com.hospital.backend.Models.Day;
import com.hospital.backend.Models.Schedule;
import com.hospital.backend.Services.DaysService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@CrossOrigin
@RequestMapping("/api/days")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DaysController {

    private final DaysService daysService;

    private final DayConverter dayConverter;

    private final ScheduleConverter scheduleConverter;

    @GetMapping("/findAllDaysByScheduleId/{scheduleId}")
    public List<DayDTO> findDaysByScheduleId(@PathVariable Long scheduleId) {
        Schedule schedule = new Schedule();
        schedule.setId(scheduleId);
        List<Day> days = daysService.findAllByScheduleId(schedule);
        return dayConverter.convertModelListToDtoList(days);

    }
}
