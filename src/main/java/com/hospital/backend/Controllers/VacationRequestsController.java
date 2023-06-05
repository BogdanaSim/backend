package com.hospital.backend.Controllers;

import com.hospital.backend.Converters.VacationRequestConverter;
import com.hospital.backend.DTOs.DayDTO;
import com.hospital.backend.DTOs.ScheduleDTO;
import com.hospital.backend.DTOs.UserDTO;
import com.hospital.backend.DTOs.VacationRequestDTO;
import com.hospital.backend.Models.Day;
import com.hospital.backend.Models.Schedule;
import com.hospital.backend.Models.User;
import com.hospital.backend.Models.VacationRequest;
import com.hospital.backend.Services.VacationRequestsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
//@CrossOrigin
@RequestMapping("/api/requests")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VacationRequestsController {

    private final VacationRequestsService vacationRequestsService;

    private final VacationRequestConverter vacationRequestConverter;

    @GetMapping("/findRequestsWithinDateRange/{start}/{end}")
    public List<VacationRequestDTO> findRequestsWithinDateRange(@PathVariable LocalDate start, @PathVariable LocalDate end) {

        List<VacationRequest> vacationRequests = vacationRequestsService.findRequestsWithinDateRange(start,end);
        return vacationRequestConverter.convertModelListToDtoList(vacationRequests);

    }
    @GetMapping("/findIntersectingRequests/{start}/{end}/{role}/{departmentId}/{status}")
    public List<VacationRequestDTO> findIntersectingRequests(@PathVariable LocalDate start, @PathVariable LocalDate end, @PathVariable Long departmentId, @PathVariable String role, @PathVariable String status) {
        List<VacationRequest> vacationRequests = vacationRequestsService.findIntersectingRequests(start,end,role,departmentId,status);
        return vacationRequestConverter.convertModelListToDtoList(vacationRequests);

    }

    @GetMapping("/findIntersectingRequestsSize/{start}/{end}/{role}/{departmentId}/{status}")
    public Integer findIntersectingRequestsSize(@PathVariable LocalDate start, @PathVariable LocalDate end, @PathVariable Long departmentId, @PathVariable String role, @PathVariable String status) {
        return vacationRequestsService.findSizeIntersectingRequests(start,end,role,departmentId,status);

    }

    @GetMapping("/findByStatusAndUserDepartment/{status}/{departmentId}")
    public List<VacationRequestDTO> findByStatusAndUserDepartment(@PathVariable Long departmentId, @PathVariable String status) {

        List<VacationRequest> vacationRequests = vacationRequestsService.findByStatusAndUserDepartment(status,departmentId);
        return vacationRequestConverter.convertModelListToDtoList(vacationRequests);

    }

    @PutMapping("/updateRequest")
    public VacationRequestDTO updateRequest(@RequestBody VacationRequestDTO vacationRequestDTO) {
        VacationRequest vacationRequest = vacationRequestConverter.convertDtoToModel(vacationRequestDTO);
        VacationRequest vacationRequest1 = vacationRequestsService.update(vacationRequest);
        return vacationRequestConverter.convertModelToDto(vacationRequest1);
    }

    @PostMapping("/addRequest")
    public VacationRequestDTO addRequest(@RequestBody VacationRequestDTO vacationRequestDTO) {
        VacationRequest vacationRequest = vacationRequestConverter.convertDtoToModel(vacationRequestDTO);
        VacationRequest vacationRequest1 = vacationRequestsService.save(vacationRequest);
        return vacationRequestConverter.convertModelToDto(vacationRequest1);
    }

}
