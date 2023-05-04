package com.hospital.backend.Services;

import com.hospital.backend.Models.Shift;

import java.util.List;
import java.util.Optional;

public interface IShiftsService {
    Shift save(Shift shift);

    void deleteById(Long id);

    List<Shift> findByUserId(Long id);

    Shift findById(Long id);

    Shift update(Shift shift);

    Optional<Shift> findByUserAndDay(Long userId, Long dayId);

}
