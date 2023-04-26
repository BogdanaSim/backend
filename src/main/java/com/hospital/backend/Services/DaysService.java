package com.hospital.backend.Services;

import com.hospital.backend.Exceptions.DayNotFoundException;
import com.hospital.backend.Models.Day;
import com.hospital.backend.Repositories.DaysRepository;
import com.hospital.backend.Repositories.ShiftsRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DaysService implements IDaysService{

    private final DaysRepository daysRepository;

    private final ShiftsRepository shiftsRepository;

    private static final Logger logger = LoggerFactory.getLogger(DaysService.class);

    @Override
    public Day save(Day day) {
        logger.info("save day: " + day.getDate());
        this.daysRepository.findById(day.getId()).orElseThrow(DayNotFoundException::new);
        return daysRepository.save(day);
    }

    @Override
    public void deleteById(Long id) {
        logger.info("delete day: " + id);
        daysRepository.findById(id).orElseThrow(DayNotFoundException::new);
        daysRepository.deleteById(id);
    }

    @Override
    public Day findById(Long id) {
        logger.info("find day with id: " + id);
        return daysRepository.findById(id).orElseThrow(DayNotFoundException::new);

    }

    @Override
    public Day update(Day day) {
        logger.info("update day: " + day.getId());
        daysRepository.findById(day.getId()).orElseThrow(DayNotFoundException::new);
        return daysRepository.save(day);
    }
}
