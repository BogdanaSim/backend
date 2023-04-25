package com.hospital.backend.Services;

import com.hospital.backend.Exceptions.DayNotFoundException;
import com.hospital.backend.Exceptions.ScheduleNotFoundException;
import com.hospital.backend.Models.Schedule;
import com.hospital.backend.Repositories.DaysRepository;
import com.hospital.backend.Repositories.SchedulesRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ScheduleService implements IScheduleService{

    private final SchedulesRepository schedulesRepository;

    private static final Logger logger = LoggerFactory.getLogger(ScheduleService.class);


    @Override
    public Schedule save(Schedule schedule) {
        logger.info("save schedule: " + schedule.getDate());
        this.schedulesRepository.findById(schedule.getId()).orElseThrow(ScheduleNotFoundException::new);
        return schedulesRepository.save(schedule);
    }

    @Override
    public void deleteById(Long id) {
        logger.info("delete schedule: " + id);
        schedulesRepository.findById(id).orElseThrow(ScheduleNotFoundException::new);
        schedulesRepository.deleteById(id);
    }

    @Override
    public Schedule findById(Long id) {
        logger.info("find schedule with id: " + id);
        return schedulesRepository.findById(id).orElseThrow(ScheduleNotFoundException::new);

    }

    @Override
    public Schedule update(Schedule schedule) {
        logger.info("update schedule: " + schedule.getId());
        schedulesRepository.findById(schedule.getId()).orElseThrow(ScheduleNotFoundException::new);
        return schedulesRepository.save(schedule);
    }
}
