package com.hospital.backend.Services;

import com.hospital.backend.Exceptions.DayNotFoundException;
import com.hospital.backend.Exceptions.ScheduleNotFoundException;
import com.hospital.backend.Models.Day;
import com.hospital.backend.Models.Schedule;
import com.hospital.backend.Repositories.DaysRepository;
import com.hospital.backend.Repositories.SchedulesRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ScheduleService implements IScheduleService{

    private final SchedulesRepository schedulesRepository;
    private final DaysRepository daysRepository;

    private static final Logger logger = LoggerFactory.getLogger(ScheduleService.class);


    @Override
    public Schedule save(Schedule schedule) {
        logger.info("save schedule: " + schedule.getDate());
        Optional<Schedule> scheduleOptional = this.schedulesRepository.findByDate(schedule.getDate());
        scheduleOptional.ifPresent(value -> schedule.setId(value.getId()));
        //this.schedulesRepository.findByDate(schedule.getDate()).orElseThrow(ScheduleNotFoundException::new);
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

    public void addNewDaysSchedule(Schedule schedule){
        int year = schedule.getDate().getYear();
        Month month = schedule.getDate().getMonth();
        YearMonth ym = YearMonth.of(year,month);
        LocalDate firstOfMonth = ym.atDay(1);
        LocalDate firstOfFollowingMonth = ym.plusMonths(1).atDay(1);
       // firstOfMonth.datesUntil(firstOfFollowingMonth).forEach(System.out::println);
        firstOfMonth.datesUntil(firstOfFollowingMonth).forEach(date -> {
            Day day = new Day();
            day.setSchedule(schedule);
            day.setDate(date);
            Optional<Day> dayOptional = this.daysRepository.findByDate(date);
            dayOptional.ifPresent(value -> day.setId(value.getId()));
            if(dayOptional.isEmpty()){
                daysRepository.save(day);
            }

        });

       // System.out.println(LocalDate.now().with(TemporalAdjusters.firstDayOfNextMonth()));
       // System.out.println(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()));
    }
}
