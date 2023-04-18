package com.hospital.backend.Repositories;

import com.hospital.backend.Models.Day;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DaysRepository extends JpaRepository<Day,Long> {

    Optional<Day> findById(Long id);

    Optional<Day> findFirstByDate(LocalDateTime date);


}
