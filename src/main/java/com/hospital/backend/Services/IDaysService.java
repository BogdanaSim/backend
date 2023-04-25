package com.hospital.backend.Services;

import com.hospital.backend.Models.Day;
import com.hospital.backend.Models.Shift;
import com.hospital.backend.Models.UserProfile;

public interface IDaysService {
    Day save(Day day);

    void deleteById(Long id);


    Day findById(Long id);

    Day update(Day day);

}
