package com.hospital.backend.Repositories;

import com.hospital.backend.Models.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShiftsRepository extends JpaRepository<Shift,Long> {
    List<Shift> findAllByUserId(Long userId);
    List<Shift> findAllByType(String type);
    Optional<Shift> findByUserIdAndDay_Id(Long user_id, Long day_id);



}
