package com.hospital.backend.Services;

import com.hospital.backend.Models.Invite;

import java.util.List;

public interface IInvitesService {
    List<Invite> findAllByDepartment(Long id);
    Invite findByEmail(String email);
    Invite save(Invite invite);
    void deleteById(Long id);

}
