package com.hospital.backend.Converters;

import com.hospital.backend.DTOs.InviteDTO;
import com.hospital.backend.Models.Department;
import com.hospital.backend.Models.Invite;
import com.hospital.backend.Models.RoleStaff;
import com.hospital.backend.Models.RoleUser;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class InviteConverter implements IConverter<Invite, InviteDTO>{
    @Override
    public Invite convertDtoToModel(InviteDTO inviteDTO) {
        Invite invite = new Invite();
        if(inviteDTO.getId()!=null){
            invite.setId(inviteDTO.getId());
        }
        invite.setEmail(inviteDTO.getEmail());
        invite.setRoleStaff(RoleStaff.valueOf(inviteDTO.getRoleStaff()));
        invite.setRoleUser(RoleUser.valueOf(inviteDTO.getRoleUser()));
        Department department = new Department();
        department.setId(inviteDTO.getDepartmentId());
        invite.setDepartment(department);

        return invite;
    }

    @Override
    public InviteDTO convertModelToDto(Invite invite) {
        InviteDTO inviteDTO = new InviteDTO();
        if(invite.getId()==null)
            return inviteDTO;
        inviteDTO.setId(invite.getId());
        inviteDTO.setEmail(invite.getEmail());
        inviteDTO.setRoleStaff(invite.getRoleStaff().toString());
        inviteDTO.setRoleUser(invite.getRoleUser().toString());
        inviteDTO.setDepartmentId(invite.getDepartment().getId());
        return inviteDTO;
    }

    @Override
    public List<InviteDTO> convertModelListToDtoList(List<Invite> invites) {
        return invites
                .stream()
                .map(this::convertModelToDto)
                .collect(Collectors.toList());
    }
}
