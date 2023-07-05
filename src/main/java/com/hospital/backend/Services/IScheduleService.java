package com.hospital.backend.Services;

import com.hospital.backend.Models.Schedule;

public interface IScheduleService {
    Schedule save(Schedule schedule);

    void deleteById(Long id);


    Schedule findById(Long id);

    Schedule update(Schedule schedule);
}
