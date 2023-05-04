package com.hospital.backend.Controllers;

import com.hospital.backend.Converters.ShiftConverter;
import com.hospital.backend.DTOs.ShiftDTO;
import com.hospital.backend.Models.Shift;
import com.hospital.backend.Services.ShiftsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
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
}