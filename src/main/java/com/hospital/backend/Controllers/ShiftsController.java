package com.hospital.backend.Controllers;

import com.hospital.backend.Converters.ShiftConverter;
import com.hospital.backend.DTOs.ShiftDTO;
import com.hospital.backend.Models.Shift;
import com.hospital.backend.Services.ShiftsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
//@CrossOrigin
@RequestMapping("/api/shifts")
@CrossOrigin(origins  ={ "http://localhost:4200","https://hssh.azurewebsites.net" }, maxAge = 3600, allowCredentials="true")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShiftsController {

    private final ShiftsService shiftsService;

    private final ShiftConverter shiftConverter;

    @GetMapping("/findAllShiftsByUserId/{userId}")
    public List<ShiftDTO> findAllFriendRequestsBySender(@PathVariable Long userId) {
        List<Shift> shifts = shiftsService.findByUserId(userId);
        return shiftConverter.convertModelListToDtoList(shifts);
    }

    @GetMapping("/findShiftByUserIdAndDayId/{userId}/{dayId}")
    public ShiftDTO findByUserIdAndDayId(@PathVariable Long userId, @PathVariable Long dayId) {
        Optional<Shift> shift = shiftsService.findByUserAndDay(userId, dayId);

        return shift.map(shiftConverter::convertModelToDto).orElse(null);
    }

    @GetMapping("/isExistShiftByUserIdAndDayId/{userId}/{dayId}")
    public boolean isExistByUserIdAndDayId(@PathVariable Long userId, @PathVariable Long dayId) {
        Optional<Shift> shift = shiftsService.findByUserAndDay(userId, dayId);
        return shift.isPresent();
    }

    @PostMapping("/addShift")
    public ShiftDTO addShift(@RequestBody ShiftDTO shiftDTO) {
        Shift shift = shiftConverter.convertDtoToModel(shiftDTO);
        Shift addedShift = shiftsService.save(shift);
        return shiftConverter.convertModelToDto(addedShift);
    }

    @PutMapping("/updateShift")
    public ShiftDTO updateShift(@RequestBody ShiftDTO shiftDTO) {
        Shift shift = shiftConverter.convertDtoToModel(shiftDTO);
        Shift updatedShift = shiftsService.update(shift);
        return shiftConverter.convertModelToDto(updatedShift);
    }

    @DeleteMapping("/deleteShift/{id}")
    public void deleteShift(@PathVariable Long id) {
        shiftsService.deleteById(id);
    }

    @GetMapping("/findByUserAndSchedule/{userId}/{scheduleId}")
    public List<ShiftDTO> findByUserAndSchedule(@PathVariable Long userId, @PathVariable Long scheduleId) {
        List<Shift> shifts = shiftsService.findByUserAndSchedule(userId,scheduleId);
        return shiftConverter.convertModelListToDtoList(shifts);
    }
}