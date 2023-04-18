package com.hospital.backend.Repositories;

import com.hospital.backend.Models.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchedulesRepository extends JpaRepository<Schedule,Long> {

}
