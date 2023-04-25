package com.hospital.backend.Services;

import com.hospital.backend.Models.Shift;
import com.hospital.backend.Models.UserProfile;

public interface IShiftsService {
    Shift save(Shift shift);

    void deleteById(Long id);

    UserProfile findByUserId(Long id);

    Shift findById(Long id);

    Shift update(Shift shift);


}
