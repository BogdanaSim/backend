package com.hospital.backend.Services;

import com.hospital.backend.Models.VacationRequest;

import java.time.LocalDate;
import java.util.List;

public interface IVacationRequestsService {

    VacationRequest save(VacationRequest vacationRequest);
    void deleteById(Long id);
    VacationRequest findById(Long id);
    VacationRequest update(VacationRequest vacationRequest);
    List<VacationRequest> findRequestsWithinDateRange(LocalDate start, LocalDate end);
}
