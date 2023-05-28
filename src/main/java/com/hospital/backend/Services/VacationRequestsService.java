package com.hospital.backend.Services;

import com.hospital.backend.Exceptions.DayNotFoundException;
import com.hospital.backend.Exceptions.VacationRequestNotFoundException;
import com.hospital.backend.Models.VacationRequest;
import com.hospital.backend.Repositories.VacationRequestsRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VacationRequestsService implements IVacationRequestsService{

    private final VacationRequestsRepository vacationRequestsRepository;
    private static final Logger logger = LoggerFactory.getLogger(VacationRequestsService.class);

    @Override
    public VacationRequest save(VacationRequest vacationRequest) {
        logger.info("save vacation request: " + vacationRequest.getStartDate()+"-"+vacationRequest.getEndDate());
//        this.vacationRequestsRepository.findById(vacationRequest.getId()).orElseThrow(VacationRequestNotFoundException::new);
        return vacationRequestsRepository.save(vacationRequest);
    }

    @Override
    public void deleteById(Long id) {
        logger.info("delete vacation request: " + id);
        vacationRequestsRepository.findById(id).orElseThrow(DayNotFoundException::new);
        vacationRequestsRepository.deleteById(id);
    }

    @Override
    public VacationRequest findById(Long id) {
        logger.info("find vacation request with id: " + id);
        return vacationRequestsRepository.findById(id).orElseThrow(VacationRequestNotFoundException::new);
    }

    @Override
    public VacationRequest update(VacationRequest vacationRequest) {
        logger.info("update vacation request: " + vacationRequest.getId());
        vacationRequestsRepository.findById(vacationRequest.getId()).orElseThrow(VacationRequestNotFoundException::new);
        return vacationRequestsRepository.save(vacationRequest);
    }

    @Override
    public List<VacationRequest> findRequestsWithinDateRange(LocalDate start, LocalDate end) {
        return vacationRequestsRepository.findRequestsWithinDateRange(start,end);
    }
}
