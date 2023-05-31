package com.hospital.backend.Services;

import com.hospital.backend.Exceptions.*;
import com.hospital.backend.Models.Schedule;
import com.hospital.backend.Models.Shift;
import com.hospital.backend.Repositories.ShiftsRepository;
import com.hospital.backend.Repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShiftsService implements IShiftsService {
    private final ShiftsRepository shiftsRepository;
    private final UsersRepository usersRepository;

    private static final Logger logger = LoggerFactory.getLogger(ShiftsService.class);

    @Override
    public Shift save(Shift shift) {
        logger.info(String.format("Saving shift with id %d", shift.getId()));
        return shiftsRepository.save(shift);
    }

    @Override
    public void deleteById(Long id) {
        logger.info("deleteById shifts: " + id);
        shiftsRepository.findById(id).orElseThrow(ShiftNotFoundException::new);
        shiftsRepository.deleteById(id);
    }

    @Override
    public List<Shift> findByUserId(Long id) {
        return shiftsRepository.findAllByUserId(id);
    }

    @Override
    public Shift findById(Long id) {
        logger.info(String.format("Finding shift with id %s", id));
        return this.shiftsRepository.findById(id).orElseThrow(ShiftNotFoundException::new);

    }

    @Override
    public Shift update(Shift shift) {
        logger.info("update: " + shift.getId());
        shiftsRepository.findById(shift.getId()).orElseThrow(ShiftNotFoundException::new);
        return shiftsRepository.save(shift);
    }

    @Override
    public Optional<Shift> findByUserAndDay(Long userId, Long dayId) {
        return this.shiftsRepository.findByUserIdAndDay_Id(userId,dayId);

    }

    public boolean isExistByUserAndDay(Long userId, Long dayId) {
        return this.shiftsRepository.findByUserIdAndDay_Id(userId,dayId).isPresent();
    }

    public List<Shift> findByUserAndSchedule(Long userId, Long scheduleId){
        Schedule schedule = new Schedule();
        schedule.setId(scheduleId);
        return this.shiftsRepository.findByUserIdAndDay_Schedule(userId,schedule);
    }
}
