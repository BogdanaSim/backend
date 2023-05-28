package com.hospital.backend.Repositories;

import com.hospital.backend.Models.Day;
import com.hospital.backend.Models.Schedule;
import com.hospital.backend.Models.StatusRequest;
import com.hospital.backend.Models.VacationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface VacationRequestsRepository extends JpaRepository<VacationRequest,Long> {
    Optional<VacationRequest> findById(Long id);

    @Query("SELECT e FROM VacationRequest e WHERE (e.startDate >= ?1 AND e.startDate <= ?2) OR (e.endDate >= ?1 AND e.endDate <= ?2) AND e.status = 'APPROVED'")
    List<VacationRequest> findRequestsWithinDateRange(LocalDate start, LocalDate end);
}
