package com.hospital.backend.Services;

import com.hospital.backend.Exceptions.*;
import com.hospital.backend.Models.Shift;
import com.hospital.backend.Models.User;
import com.hospital.backend.Models.UserProfile;
import com.hospital.backend.Repositories.ShiftsRepository;
import com.hospital.backend.Repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        usersRepository.findById(id).orElseThrow(ShiftNotFoundException::new);
        usersRepository.deleteById(id);
    }

    @Override
    public UserProfile findByUserId(Long id) {
        return null;
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
}
