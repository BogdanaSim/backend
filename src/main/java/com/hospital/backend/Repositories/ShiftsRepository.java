package com.hospital.backend.Repositories;

import com.hospital.backend.Models.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShiftsRepository extends JpaRepository<Shift,Long> {
    List<Shift> findAllByUserId(Long userId);
    List<Shift> findAllByType(String type);


}
