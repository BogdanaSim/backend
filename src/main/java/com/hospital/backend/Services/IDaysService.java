package com.hospital.backend.Services;

import com.hospital.backend.Models.Day;
import com.hospital.backend.Models.Schedule;

import java.util.List;

public interface IDaysService {
    Day save(Day day);

    void deleteById(Long id);


    Day findById(Long id);

    Day update(Day day);

    List<Day> findAllByScheduleId(Schedule schedule);

}
