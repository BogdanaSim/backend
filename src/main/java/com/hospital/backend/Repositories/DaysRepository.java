package com.hospital.backend.Repositories;

import com.hospital.backend.Models.Day;
import com.hospital.backend.Models.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DaysRepository extends JpaRepository<Day, Long> {

    Optional<Day> findById(Long id);

    Optional<Day> findFirstByDate(LocalDate date);

    Optional<Day> findByDate(LocalDate date);

    List<Day> findDaysBySchedule(Schedule schedule);

}
