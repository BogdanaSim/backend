package com.hospital.backend.Controllers;

import com.hospital.backend.Converters.InviteConverter;
import com.hospital.backend.DTOs.InviteDTO;
import com.hospital.backend.DTOs.ShiftDTO;
import com.hospital.backend.DTOs.UserDTO;
import com.hospital.backend.Models.Invite;
import com.hospital.backend.Models.Shift;
import com.hospital.backend.Models.User;
import com.hospital.backend.Services.InvitesService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@CrossOrigin
@RequestMapping("/api/invites")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))

public class InvitesController {
    private final InvitesService invitesService;
    private final InviteConverter inviteConverter;

    @GetMapping("/findAllByDepartment/{idDepartment}")
    public List<InviteDTO> findAllByDepartment(@PathVariable Long idDepartment) {
        List<Invite> invites = invitesService.findAllByDepartment(idDepartment);
        return inviteConverter.convertModelListToDtoList(invites);
    }

    @GetMapping("/findInviteByEmail/{email}")
    public InviteDTO findInviteByEmail(@PathVariable String email) {
        return inviteConverter.convertModelToDto(invitesService.findByEmail(email));
    }

    @PostMapping("/addInvite")
    public InviteDTO addShift(@RequestBody InviteDTO inviteDTO) {
        Invite invite = inviteConverter.convertDtoToModel(inviteDTO);
        Invite addedInvite = invitesService.save(invite);
        return inviteConverter.convertModelToDto(addedInvite);
    }

    @DeleteMapping("/deleteInvite/{id}")
    public void deleteInvite(@PathVariable Long id) {
        invitesService.deleteById(id);
    }

}
