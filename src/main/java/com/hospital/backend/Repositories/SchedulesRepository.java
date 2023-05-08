package com.hospital.backend.Repositories;

import com.hospital.backend.Models.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SchedulesRepository extends JpaRepository<Schedule,Long> {
    Boolean existsByDate(LocalDate date);
    Optional<Schedule> findByDate(LocalDate date);
}
