package com.hospital.backend.Controllers;

import com.hospital.backend.Converters.VacationRequestConverter;
import com.hospital.backend.DTOs.DayDTO;
import com.hospital.backend.DTOs.VacationRequestDTO;
import com.hospital.backend.Models.Day;
import com.hospital.backend.Models.Schedule;
import com.hospital.backend.Models.VacationRequest;
import com.hospital.backend.Services.VacationRequestsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VacationRequestsController {

    private final VacationRequestsService vacationRequestsService;

    private final VacationRequestConverter vacationRequestConverter;

    @GetMapping("/findRequestsWithinDateRange/{start}/{end}")
    public List<VacationRequestDTO> findRequestsWithinDateRange(@PathVariable LocalDate start, @PathVariable LocalDate end) {

        List<VacationRequest> vacationRequests = vacationRequestsService.findRequestsWithinDateRange(start,end);
        return vacationRequestConverter.convertModelListToDtoList(vacationRequests);

    }
    @GetMapping("/findByStatusAndUserDepartment/{status}/{departmentId}")
    public List<VacationRequestDTO> findByStatusAndUserDepartment(@PathVariable Long departmentId, @PathVariable String status) {

        List<VacationRequest> vacationRequests = vacationRequestsService.findByStatusAndUserDepartment(status,departmentId);
        return vacationRequestConverter.convertModelListToDtoList(vacationRequests);

    }

}
